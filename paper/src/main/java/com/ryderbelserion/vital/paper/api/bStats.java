package com.ryderbelserion.vital.paper.api;

import com.ryderbelserion.vital.paper.util.MiscUtil;
import org.bukkit.Server;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.zip.GZIPOutputStream;

/**
 * Sends metrics to <a href="https://bstats.org">bstats</a>
 *
 * @author Bastian
 * @since 0.0.1
 */
public class bStats {

    private final JavaPlugin plugin;

    private final MetricsBase metricsBase;

    /**
     * Creates a new Metrics instance.
     *
     * @param plugin the plugin instance
     * @param serviceId the id of the service. It can be found at <a href="https://bstats.org/what-is-my-plugin-id">What is my plugin id?</a>
     * @since 0.0.1
     */
    public bStats(JavaPlugin plugin, int serviceId) {
        this.plugin = plugin;
        // Get the config file
        File bStatsFolder = new File(plugin.getDataFolder().getParentFile(), "bStats");
        File configFile = new File(bStatsFolder, "config.yml");

        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        if (!config.isSet("serverUuid")) {
            config.addDefault("enabled", true);
            config.addDefault("serverUuid", UUID.randomUUID().toString());
            config.addDefault("logFailedRequests", false);
            config.addDefault("logSentData", false);
            config.addDefault("logResponseStatusText", false);

            // Inform the server owners about bStats
            config
                    .options()
                    .setHeader(
                            List.of(
                                    "bStats (https://bStats.org) collects some basic information for plugin authors, like how",
                                    "many people use their plugin and their total player count. It's recommended to keep bStats",
                                    "enabled, but if you're not comfortable with this, you can turn this setting off. There is no",
                                    "performance penalty associated with having metrics enabled, and data sent to bStats is fully",
                                    "anonymous."
                            ))
                    .copyDefaults(true);
            try {
                config.save(configFile);
            } catch (IOException ignored) {}
        }

        // Load the data
        boolean enabled = config.getBoolean("enabled", true);
        String serverUUID = config.getString("serverUuid");
        boolean logErrors = config.getBoolean("logFailedRequests", false);
        boolean logSentData = config.getBoolean("logSentData", false);
        boolean logResponseStatusText = config.getBoolean("logResponseStatusText", false);

        this.metricsBase = new MetricsBase(
                        "bukkit",
                        serverUUID,
                        serviceId,
                        enabled,
                        this::appendPlatformData,
                        this::appendServiceData,
                        submitDataTask -> plugin.getServer().getGlobalRegionScheduler().execute(plugin, submitDataTask),
                        plugin::isEnabled,
                        (message, error) -> plugin.getLogger().log(Level.WARNING, message, error),
                        (message) -> plugin.getLogger().log(Level.INFO, message),
                        logErrors,
                        logSentData,
                        logResponseStatusText);
    }

    /**
     * Shuts down the underlying scheduler service.
     *
     * @since 0.0.1
     */
    public void shutdown() {
        this.metricsBase.shutdown();
    }

    /**
     * Checks if bstats is enabled.
     *
     * @return true or false
     * @since 0.0.1
     */
    protected boolean isEnabled() {
        if (this.metricsBase == null) return false;

        return this.metricsBase.enabled;
    }

    /**
     * Adds a custom chart.
     *
     * @param chart The chart to add.
     * @since 0.0.1
     */
    public void addCustomChart(CustomChart chart) {
        this.metricsBase.addCustomChart(chart);
    }

    private void appendPlatformData(JsonObjectBuilder builder) {
        final Server server = this.plugin.getServer();

        builder.appendField("playerAmount", getPlayerAmount());
        builder.appendField("onlineMode", server.getOnlineMode() ? 1 : 0);
        builder.appendField("bukkitVersion", server.getVersion());
        builder.appendField("bukkitName", server.getName());
        builder.appendField("javaVersion", System.getProperty("java.version"));
        builder.appendField("osName", System.getProperty("os.name"));
        builder.appendField("osArch", System.getProperty("os.arch"));
        builder.appendField("osVersion", System.getProperty("os.version"));
        builder.appendField("coreCount", Runtime.getRuntime().availableProcessors());
    }

    private void appendServiceData(JsonObjectBuilder builder) {
        builder.appendField("pluginVersion", this.plugin.getPluginMeta().getVersion());
    }

    private int getPlayerAmount() {
        return this.plugin.getServer().getOnlinePlayers().size();
    }

    /**
     * Metrics class
     *
     * @author Bastian
     * @since 0.0.1
     */
    public static class MetricsBase {

        /**
         * The version of the Metrics class.
         */
        public static final String METRICS_VERSION = "3.0.2";

        private static final String REPORT_URL = "https://bStats.org/api/v2/data/%s";

        private final ScheduledExecutorService scheduler;

        private final String platform;

        private final String serverUuid;

        private final int serviceId;

        private final Consumer<JsonObjectBuilder> appendPlatformDataConsumer;

        private final Consumer<JsonObjectBuilder> appendServiceDataConsumer;

        private final Consumer<Runnable> submitTaskConsumer;

        private final Supplier<Boolean> checkServiceEnabledSupplier;

        private final BiConsumer<String, Throwable> errorLogger;

        private final Consumer<String> infoLogger;

        private final boolean logErrors;

        private final boolean logSentData;

        private final boolean logResponseStatusText;

        private final Set<CustomChart> customCharts = new HashSet<>();

        private final boolean enabled;

        /**
         * Creates a new MetricsBase class instance.
         *
         * @param platform                    The platform of the service.
         * @param serviceId                   The id of the service.
         * @param serverUuid                  The server uuid.
         * @param enabled                     Whether data sending is enabled.
         * @param appendPlatformDataConsumer  A consumer that receives a {@code JsonObjectBuilder} and
         *                                    appends all platform-specific data.
         * @param appendServiceDataConsumer   A consumer that receives a {@code JsonObjectBuilder} and
         *                                    appends all service-specific data.
         * @param submitTaskConsumer          A consumer that takes a runnable with the submit task. This can be
         *                                    used to delegate the data collection to a another thread to prevent errors caused by
         *                                    concurrency. Can be {@code null}.
         * @param checkServiceEnabledSupplier A supplier to check if the service is still enabled.
         * @param errorLogger                 A consumer that accepts log message and an error.
         * @param infoLogger                  A consumer that accepts info log messages.
         * @param logErrors                   Whether errors should be logged.
         * @param logSentData                 Whether the scent data should be logged.
         * @param logResponseStatusText       Whether the response status text should be logged.
         * @since 0.0.1
         */
        public MetricsBase(
                String platform,
                String serverUuid,
                int serviceId,
                boolean enabled,
                Consumer<JsonObjectBuilder> appendPlatformDataConsumer,
                Consumer<JsonObjectBuilder> appendServiceDataConsumer,
                Consumer<Runnable> submitTaskConsumer,
                Supplier<Boolean> checkServiceEnabledSupplier,
                BiConsumer<String, Throwable> errorLogger,
                Consumer<String> infoLogger,
                boolean logErrors,
                boolean logSentData,
                boolean logResponseStatusText) {
            ScheduledThreadPoolExecutor scheduler = new ScheduledThreadPoolExecutor(1, task -> new Thread(task, "bStats-Metrics"));

            // We want delayed tasks (non-periodic) that will execute in the future to be
            // cancelled when the scheduler is shutdown.
            // Otherwise, we risk preventing the server from shutting down even when
            // MetricsBase#shutdown() is called
            scheduler.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);

            this.scheduler = scheduler;
            this.platform = platform;
            this.serverUuid = serverUuid;
            this.serviceId = serviceId;
            this.enabled = enabled;
            this.appendPlatformDataConsumer = appendPlatformDataConsumer;
            this.appendServiceDataConsumer = appendServiceDataConsumer;
            this.submitTaskConsumer = submitTaskConsumer;
            this.checkServiceEnabledSupplier = checkServiceEnabledSupplier;
            this.errorLogger = errorLogger;
            this.infoLogger = infoLogger;
            this.logErrors = logErrors;
            this.logSentData = logSentData;
            this.logResponseStatusText = logResponseStatusText;

            checkRelocation();

            if (enabled) {
                // WARNING: Removing the option to opt-out will get your plugin banned from
                // bStats
                startSubmitting();
            }
        }

        /**
         * Gzips the given string.
         *
         * @param str The string to gzip.
         * @return The gzipped string.
         * @since 0.0.1
         */
        private static byte[] compress(final String str) throws IOException {
            if (str == null) {
                return null;
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            try (GZIPOutputStream gzip = new GZIPOutputStream(outputStream)) {
                gzip.write(str.getBytes(StandardCharsets.UTF_8));
            }

            return outputStream.toByteArray();
        }

        /**
         * Adds custom chart
         *
         * @param chart {@link CustomChart}
         * @since 0.0.1
         */
        public void addCustomChart(CustomChart chart) {
            this.customCharts.add(chart);
        }

        /**
         * Shuts down scheduler
         *
         * @since 0.0.1
         */
        public void shutdown() {
            this.scheduler.shutdown();
        }

        /**
         * Submit to bstats
         *
         * @since 0.0.1
         */
        private void startSubmitting() {
            final Runnable submitTask =
                    () -> {
                        if (!this.enabled || !this.checkServiceEnabledSupplier.get()) {
                            // Submitting data or service is disabled
                            this.scheduler.shutdown();

                            return;
                        }

                        if (this.submitTaskConsumer != null) {
                            this.submitTaskConsumer.accept(this::submitData);
                        } else {
                            this.submitData();
                        }
                    };

            // Many servers tend to restart at a fixed time at xx:00 which causes an uneven
            // distribution of requests on the
            // bStats backend. To circumvent this problem, we introduce some randomness into
            // the initial and second delay.
            // WARNING: You must not modify and part of this Metrics class, including the
            // submit delay or frequency!
            // WARNING: Modifying this code will get your plugin banned on bStats. Just
            // don't do it!
            long initialDelay = (long) (1000 * 60 * (3 + Math.random() * 3));
            long secondDelay = (long) (1000 * 60 * (Math.random() * 30));
            this.scheduler.schedule(submitTask, initialDelay, TimeUnit.MILLISECONDS);
            this.scheduler.scheduleAtFixedRate(submitTask, initialDelay + secondDelay, 1000 * 60 * 30, TimeUnit.MILLISECONDS);
        }

        private void submitData() {
            final JsonObjectBuilder baseJsonBuilder = new JsonObjectBuilder();
            this.appendPlatformDataConsumer.accept(baseJsonBuilder);
            final JsonObjectBuilder serviceJsonBuilder = new JsonObjectBuilder();
            this.appendServiceDataConsumer.accept(serviceJsonBuilder);

            JsonObjectBuilder.JsonObject[] chartData =
                    this.customCharts.stream()
                            .map(customChart -> customChart.getRequestJsonObject(this.errorLogger, this.logErrors))
                            .filter(Objects::nonNull)
                            .toArray(JsonObjectBuilder.JsonObject[]::new);

            serviceJsonBuilder.appendField("id", this.serviceId);
            serviceJsonBuilder.appendField("customCharts", chartData);
            baseJsonBuilder.appendField("service", serviceJsonBuilder.build());
            baseJsonBuilder.appendField("serverUUID", this.serverUuid);
            baseJsonBuilder.appendField("metricsVersion", METRICS_VERSION);
            JsonObjectBuilder.JsonObject data = baseJsonBuilder.build();

            this.scheduler.execute(
                    () -> {
                        try {
                            // Send the data
                            sendData(data);
                        } catch (Exception e) {
                            // Something went wrong! :(
                            if (this.logErrors) {
                                this.errorLogger.accept("Could not submit bStats metrics data", e);
                            }
                        }
                    });
        }

        /**
         * Send the data.
         *
         * @param data the payload
         * @throws Exception throw exception if it fails
         * @since 0.0.1
         */
        private void sendData(JsonObjectBuilder.JsonObject data) throws Exception {
            if (this.logSentData) {
                this.infoLogger.accept("Sent bStats metrics data: " + data.toString());
            }

            String url = String.format(REPORT_URL, this.platform);

            HttpsURLConnection connection = (HttpsURLConnection) URI.create(url).toURL().openConnection();

            // Compress the data to save bandwidth
            byte[] compressedData = compress(data.toString());

            connection.setRequestMethod("POST");
            connection.addRequestProperty("Accept", "application/json");
            connection.addRequestProperty("Connection", "close");
            connection.addRequestProperty("Content-Encoding", "gzip");
            connection.addRequestProperty("Content-Length", String.valueOf(compressedData.length));
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("User-Agent", "Metrics-Service/1");
            connection.setDoOutput(true);

            try (DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream())) {
                outputStream.write(compressedData);
            }

            StringBuilder builder = new StringBuilder();

            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    builder.append(line);
                }
            }

            if (this.logResponseStatusText) {
                this.infoLogger.accept("Sent data to bStats and received response: " + builder);
            }
        }

        /**
         * Checks that the class was properly relocated.
         *
         * @since 0.0.1
         */
        private void checkRelocation() {
            // You can use the property to disable the check in your test environment
            if (System.getProperty("bstats.relocatecheck") == null || !System.getProperty("bstats.relocatecheck").equals("false")) {
                // Maven's Relocate is clever and changes strings, too. So we have to use this
                // little "trick" ... :D
                final String defaultPackage = new String(new byte[]{'o', 'r', 'g', '.', 'b', 's', 't', 'a', 't', 's'});
                final String examplePackage = new String(new byte[]{'y', 'o', 'u', 'r', '.', 'p', 'a', 'c', 'k', 'a', 'g', 'e'});
                // We want to make sure no one just copy & pastes the example and uses the wrong package names
                if (MetricsBase.class.getPackage().getName().startsWith(defaultPackage) || MetricsBase.class.getPackage().getName().startsWith(examplePackage)) {
                    throw new IllegalStateException("bStats Metrics class has not been relocated correctly!");
                }
            }
        }
    }

    /**
     * Simple pie chart
     *
     * @author Bastian
     * @since 0.0.1
     */
    public static class SimplePie extends CustomChart {

        private final Callable<String> callable;

        /**
         * Class constructor.
         *
         * @param chartId  The id of the chart.
         * @param callable The callable which is used to request the chart data.
         * @since 0.0.1
         */
        public SimplePie(String chartId, Callable<String> callable) {
            super(chartId);
            this.callable = callable;
        }

        /**
         * Gets the chart data
         *
         * @return json object
         * @throws Exception if fetching the object fails
         * @since 0.0.1
         */
        @Override
        protected JsonObjectBuilder.JsonObject getChartData() throws Exception {
            String value = this.callable.call();

            if (value == null || value.isEmpty()) {
                // Null = skip the chart
                return null;
            }

            return new JsonObjectBuilder().appendField("value", value).build();
        }
    }

    /**
     * Multiline chart
     *
     * @author Bastian
     * @since 0.0.1
     */
    public static class MultiLineChart extends CustomChart {

        private final Callable<Map<String, Integer>> callable;

        /**
         * Class constructor.
         *
         * @param chartId  The id of the chart.
         * @param callable The callable which is used to request the chart data.
         * @since 0.0.1
         */
        public MultiLineChart(String chartId, Callable<Map<String, Integer>> callable) {
            super(chartId);
            this.callable = callable;
        }

        /**
         * Gets the chart data
         *
         * @return json object
         * @throws Exception if fetching the object fails
         * @since 0.0.1
         */
        @Override
        protected JsonObjectBuilder.JsonObject getChartData() throws Exception {
            return MiscUtil.getChartData(this.callable);
        }
    }

    /**
     * Advanced pie chart
     *
     * @author Bastian
     * @since 0.0.1
     */
    public static class AdvancedPie extends CustomChart {

        private final Callable<Map<String, Integer>> callable;

        /**
         * Class constructor.
         *
         * @param chartId  The id of the chart.
         * @param callable The callable which is used to request the chart data.
         * @since 0.0.1
         */
        public AdvancedPie(String chartId, Callable<Map<String, Integer>> callable) {
            super(chartId);
            this.callable = callable;
        }

        /**
         * Gets the chart data
         *
         * @return json object
         * @throws Exception if fetching the object fails
         * @since 0.0.1
         */
        @Override
        protected JsonObjectBuilder.JsonObject getChartData() throws Exception {
            return MiscUtil.getChartData(this.callable);
        }
    }

    /**
     * Simple bar chart
     *
     * @author Bastian
     * @since 0.0.1
     */
    public static class SimpleBarChart extends CustomChart {

        private final Callable<Map<String, Integer>> callable;

        /**
         * Class constructor.
         *
         * @param chartId  The id of the chart.
         * @param callable The callable which is used to request the chart data.
         * @since 0.0.1
         */
        public SimpleBarChart(String chartId, Callable<Map<String, Integer>> callable) {
            super(chartId);
            this.callable = callable;
        }

        /**
         * Gets the chart data
         *
         * @return json object
         * @throws Exception if fetching the object fails
         * @since 0.0.1
         */
        @Override
        protected JsonObjectBuilder.JsonObject getChartData() throws Exception {
            JsonObjectBuilder valuesBuilder = new JsonObjectBuilder();

            Map<String, Integer> map = this.callable.call();

            if (map == null || map.isEmpty()) {
                // Null = skip the chart
                return null;
            }

            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                valuesBuilder.appendField(entry.getKey(), new int[]{entry.getValue()});
            }

            return new JsonObjectBuilder().appendField("values", valuesBuilder.build()).build();
        }
    }

    /**
     * Advanced bar chart
     *
     * @author Bastian
     * @since 0.0.1
     */
    public static class AdvancedBarChart extends CustomChart {

        private final Callable<Map<String, int[]>> callable;

        /**
         * Class constructor.
         *
         * @param chartId  The id of the chart.
         * @param callable The callable which is used to request the chart data.
         * @since 0.0.1
         */
        public AdvancedBarChart(String chartId, Callable<Map<String, int[]>> callable) {
            super(chartId);
            this.callable = callable;
        }

        /**
         * Gets the chart data
         *
         * @return json object
         * @throws Exception if fetching the object fails
         * @since 0.0.1
         */
        @Override
        protected JsonObjectBuilder.JsonObject getChartData() throws Exception {
            JsonObjectBuilder valuesBuilder = new JsonObjectBuilder();

            Map<String, int[]> map = this.callable.call();

            if (map == null || map.isEmpty()) {
                // Null = skip the chart
                return null;
            }

            boolean allSkipped = true;

            for (Map.Entry<String, int[]> entry : map.entrySet()) {
                if (entry.getValue().length == 0) {
                    // Skip this invalid
                    continue;
                }

                allSkipped = false;

                valuesBuilder.appendField(entry.getKey(), entry.getValue());
            }

            if (allSkipped) {
                // Null = skip the chart
                return null;
            }

            return new JsonObjectBuilder().appendField("values", valuesBuilder.build()).build();
        }
    }

    /**
     * Drilldown chart
     *
     * @author Bastian
     * @since 0.0.1
     */
    public static class DrilldownPie extends CustomChart {

        private final Callable<Map<String, Map<String, Integer>>> callable;

        /**
         * Class constructor.
         *
         * @param chartId  The id of the chart.
         * @param callable The callable which is used to request the chart data.
         * @since 0.0.1
         */
        public DrilldownPie(String chartId, Callable<Map<String, Map<String, Integer>>> callable) {
            super(chartId);
            this.callable = callable;
        }

        /**
         * Gets the chart data
         *
         * @return json object
         * @throws Exception if fetching the object fails
         * @since 0.0.1
         */
        @Override
        public JsonObjectBuilder.JsonObject getChartData() throws Exception {
            JsonObjectBuilder valuesBuilder = new JsonObjectBuilder();

            Map<String, Map<String, Integer>> map = this.callable.call();

            if (map == null || map.isEmpty()) {
                // Null = skip the chart
                return null;
            }

            boolean reallyAllSkipped = true;

            for (Map.Entry<String, Map<String, Integer>> entryValues : map.entrySet()) {
                JsonObjectBuilder valueBuilder = new JsonObjectBuilder();

                boolean allSkipped = true;

                for (Map.Entry<String, Integer> valueEntry : map.get(entryValues.getKey()).entrySet()) {
                    valueBuilder.appendField(valueEntry.getKey(), valueEntry.getValue());
                    allSkipped = false;
                }

                if (!allSkipped) {
                    reallyAllSkipped = false;
                    valuesBuilder.appendField(entryValues.getKey(), valueBuilder.build());
                }
            }

            if (reallyAllSkipped) {
                // Null = skip the chart
                return null;
            }

            return new JsonObjectBuilder().appendField("values", valuesBuilder.build()).build();
        }
    }

    /**
     * Custom chart.
     *
     * @author Bastian
     * @since 0.0.1
     */
    public abstract static class CustomChart {

        private final String chartId;

        /**
         * Build custom chart.
         *
         * @param chartId chart id
         * @since 0.0.1
         */
        protected CustomChart(String chartId) {
            if (chartId == null) {
                throw new IllegalArgumentException("chartId must not be null");
            }

            this.chartId = chartId;
        }

        /**
         * Request json object.
         *
         * @param errorLogger the logger
         * @param logErrors true or false
         * @return json object
         * @since 0.0.1
         */
        public JsonObjectBuilder.JsonObject getRequestJsonObject(BiConsumer<String, Throwable> errorLogger, boolean logErrors) {
            JsonObjectBuilder builder = new JsonObjectBuilder();

            builder.appendField("chartId", this.chartId);

            try {
                JsonObjectBuilder.JsonObject data = getChartData();

                if (data == null) {
                    // If the data is null we don't send the chart.
                    return null;
                }

                builder.appendField("data", data);
            } catch (Throwable t) {
                if (logErrors) {
                    errorLogger.accept("Failed to get data for custom chart with id " + chartId, t);
                }

                return null;
            }

            return builder.build();
        }

        /**
         * Gets the chart data.
         *
         * @return json object
         * @throws Exception if fetching the object fails
         * @since 0.0.1
         */
        protected abstract JsonObjectBuilder.JsonObject getChartData() throws Exception;
    }

    /**
     * Single line chart.
     *
     * @author Bastian
     * @since 0.0.1
     */
    public static class SingleLineChart extends CustomChart {

        private final Callable<Integer> callable;

        /**
         * Class constructor.
         *
         * @param chartId  The id of the chart.
         * @param callable The callable which is used to request the chart data.
         * @since 0.0.1
         */
        public SingleLineChart(String chartId, Callable<Integer> callable) {
            super(chartId);

            this.callable = callable;
        }

        /**
         * Gets the chart data.
         *
         * @return json object
         * @throws Exception if fetching the object fails
         * @since 0.0.1
         */
        @Override
        protected JsonObjectBuilder.JsonObject getChartData() throws Exception {
            int value = this.callable.call();

            if (value == 0) {
                // Null = skip the chart
                return null;
            }

            return new JsonObjectBuilder().appendField("value", value).build();
        }
    }

    /**
     * An extremely simple JSON builder.
     *
     * <p>While this class is neither feature-rich nor the most performant one, it's sufficient
     * for its use-case.
     *
     * @author Bastian
     * @since 0.0.1
     */
    public static class JsonObjectBuilder {

        private StringBuilder builder = new StringBuilder();

        private boolean hasAtLeastOneField = false;

        /**
         * Creates the json object builder
         *
         * @since 0.0.1
         */
        public JsonObjectBuilder() {
            this.builder.append("{");
        }

        /**
         * Escapes the given string like stated in <a href="https://www.ietf.org/rfc/rfc4627.txt">...</a>.
         *
         * <p>This method escapes only the necessary characters '"', '\'. and '\u0000' - '\u001F'.
         * Compact escapes are not used (e.g., '\n' is escaped as "" and not as "\n").
         *
         * @param value The value to escape.
         * @return The escaped value.
         *
         * @since 0.0.1
         */
        private static String escape(String value) {
            final StringBuilder builder = new StringBuilder();

            for (int i = 0; i < value.length(); i++) {
                char c = value.charAt(i);

                if (c == '"') {
                    builder.append("\\\"");
                } else if (c == '\\') {
                    builder.append("\\\\");
                } else if (c <= '\u000F') {
                    builder.append("\\u000").append(Integer.toHexString(c));
                } else if (c <= '\u001F') {
                    builder.append("\\u00").append(Integer.toHexString(c));
                } else {
                    builder.append(c);
                }
            }

            return builder.toString();
        }

        /**
         * Appends a null field to the JSON.
         *
         * @param key The key of the field.
         * @return A reference to this object.
         * @since 0.0.1
         */
        public JsonObjectBuilder appendNull(String key) {
            appendFieldUnescaped(key, "null");

            return this;
        }

        /**
         * Appends a string field to the JSON.
         *
         * @param key   The key of the field.
         * @param value The value of the field.
         * @return A reference to this object.
         * @since 0.0.1
         */
        public JsonObjectBuilder appendField(String key, String value) {
            if (value == null) {
                throw new IllegalArgumentException("JSON value must not be null");
            }

            appendFieldUnescaped(key, "\"" + escape(value) + "\"");

            return this;
        }

        /**
         * Appends an integer field to the JSON.
         *
         * @param key   The key of the field.
         * @param value The value of the field.
         * @return A reference to this object.
         * @since 0.0.1
         */
        public JsonObjectBuilder appendField(String key, int value) {
            appendFieldUnescaped(key, String.valueOf(value));

            return this;
        }

        /**
         * Appends an object to the JSON.
         *
         * @param key    The key of the field.
         * @param object The object.
         * @return A reference to this object.
         * @since 0.0.1
         */
        public JsonObjectBuilder appendField(String key, JsonObject object) {
            if (object == null) {
                throw new IllegalArgumentException("JSON object must not be null");
            }

            appendFieldUnescaped(key, object.toString());

            return this;
        }

        /**
         * Appends a string array to the JSON.
         *
         * @param key    The key of the field.
         * @param values The string array.
         * @return A reference to this object.
         * @since 0.0.1
         */
        public JsonObjectBuilder appendField(String key, String[] values) {
            if (values == null) {
                throw new IllegalArgumentException("JSON values must not be null");
            }

            String escapedValues = Arrays.stream(values).map(value -> "\"" + escape(value) + "\"").collect(Collectors.joining(","));

            appendFieldUnescaped(key, "[" + escapedValues + "]");

            return this;
        }

        /**
         * Appends an integer array to the JSON.
         *
         * @param key    The key of the field.
         * @param values The integer array.
         * @return A reference to this object.
         * @since 0.0.1
         */
        public JsonObjectBuilder appendField(String key, int[] values) {
            if (values == null) {
                throw new IllegalArgumentException("JSON values must not be null");
            }

            String escapedValues = Arrays.stream(values).mapToObj(String::valueOf).collect(Collectors.joining(","));

            appendFieldUnescaped(key, "[" + escapedValues + "]");

            return this;
        }

        /**
         * Appends an object array to the JSON.
         *
         * @param key    The key of the field.
         * @param values The integer array.
         * @return A reference to this object.
         * @since 0.0.1
         */
        public JsonObjectBuilder appendField(String key, JsonObject[] values) {
            if (values == null) {
                throw new IllegalArgumentException("JSON values must not be null");
            }

            String escapedValues = Arrays.stream(values).map(JsonObject::toString).collect(Collectors.joining(","));

            appendFieldUnescaped(key, "[" + escapedValues + "]");

            return this;
        }

        /**
         * Appends a field to the object.
         *
         * @param key          The key of the field.
         * @param escapedValue The escaped value of the field.
         * @since 0.0.1
         */
        private void appendFieldUnescaped(String key, String escapedValue) {
            if (this.builder == null) {
                throw new IllegalStateException("JSON has already been built");
            }

            if (key == null) {
                throw new IllegalArgumentException("JSON key must not be null");
            }

            if (this.hasAtLeastOneField) {
                this.builder.append(",");
            }

            this.builder.append("\"").append(escape(key)).append("\":").append(escapedValue);

            this.hasAtLeastOneField = true;
        }

        /**
         * Builds the JSON string and invalidates this builder.
         *
         * @return The built JSON string.
         * @since 0.0.1
         */
        public JsonObject build() {
            if (this.builder == null) {
                throw new IllegalStateException("JSON has already been built");
            }

            JsonObject object = new JsonObject(this.builder.append("}").toString());

            this.builder = null;

            return object;
        }

        /**
         * A super simple representation of a JSON object.
         *
         * <p>This class only exists to make methods of the {@link JsonObjectBuilder} type-safe and not
         * allow a raw string inputs for methods like {@link JsonObjectBuilder#appendField(String,
         * JsonObject)}.
         *
         * @author Bastian
         * @since 0.0.1
         */
        public static class JsonObject {

            private final String value;

            private JsonObject(String value) {
                this.value = value;
            }

            @Override
            public String toString() {
                return this.value;
            }
        }
    }
}