package com.arielnetworks.ragnalog.port.adapter.embulk

import com.arielnetworks.ragnalog.domain.model.RegistrationService
import com.google.inject.{Guice, Module}
import org.embulk.plugin.{PluginClassLoaderFactory, PluginClassLoaderModule}

import scala.collection.JavaConversions._

class EmbulkAdapter(embulkConfiguration: EmbulkConfiguration) extends RegistrationService {

  val modules: Seq[Module] = Seq(new PluginClassLoaderModule(null))
  val injector = Guice.createInjector(modules)
  val factory = injector.getInstance(classOf[PluginClassLoaderFactory])

/*
  embulkSetting = config.getEmbulk();
  val elasticsearchSetting = config.getElasticsearch();

  logTypesSetting = embulkSetting.getTypes();

  //TODO: 設定が見つからなかったときにエラー出す
  val embulkEmbed = prepare(embulkSetting)

  uploadedDir = config.getUploader().getUploadedDir();

  generatorMap = ImmutableMap.< String, ConfigurationGenerator > builder()
    .put("grok", new GrokConfigurationGenerator(embulkSetting, elasticsearchSetting))
    .put("sar", new SarConfigurationGenerator(embulkSetting, elasticsearchSetting))
    .put("csv", new CsvConfigurationGenerator(embulkSetting, elasticsearchSetting))
    .build();


  val logTypesSetting: Map[String, TypeConfiguration]
  val factory: PluginClassLoaderFactory
  val uploadedDir: String
  val generatorMap: Map[String, ConfigurationGenerator]
  val embulkSetting: EmbulkConfiguration


  def prepare(config: EmbulkConfiguration): EmbulkEmbed = {

    val bootstrap = new EmbulkEmbed.Bootstrap()
    val systemConfig = bootstrap.getSystemConfigLoader().newConfigSource()
    systemConfig.set("log_path", config.getLogPath());
    bootstrap.setSystemConfig(systemConfig)

    for (Map.Entry < String, EmbulkConfiguration.PluginConfiguration > entry: config.getPlugins ().entrySet())
    {

      logger.info("add plugin: " + entry.getValue().getFullName());
      Path classpath = Paths.get(config.getPluginsPath(), entry.getValue().getFullName(), "classpath");

      List < URL > urls;
      try {
        urls = Files.list(classpath).map(x -> {
          try {
            return x.toUri().toURL();
          } catch (MalformedURLException e) {
            return null;
          }
        }).collect(Collectors.toList());
      } catch (IOException e) {
        logger.error(e);
        continue;
      }

      PluginClassLoader pluginLoader = factory.create(urls, Thread.currentThread().getContextClassLoader());

      try {
        Class <?> pluginClass = pluginLoader.loadClass(entry.getValue().getClassName());
        bootstrap.addModules(new Module() {
          @Override
          public void configure(Binder binder) {
            InjectedPluginSource.registerPluginTo(
              binder,
              getPluginType(entry.getValue().getType()),
              entry.getKey(),
              pluginClass
            );
          }
        });
      } catch (ClassNotFoundException e) {
        logger.error(e);
      }

    }

    return bootstrap.initialize();
  }

  //TODO: もうちょっとマシな実装にしよう
  private Class <?> getPluginType(String pluginType) {
    switch(pluginType) {
      case "input":
      return InputPlugin.class;
      case "parser":
      return ParserPlugin.class;
      case "filter":
      return FilterPlugin.class;
      case "output":
      return OutputPlugin.class;
      default:
      return null;
    }
  }

  def run (req:RegistrationRequest):Future[RegistrationResponse] = {
    try {
      val typeConfig = logTypesSetting.get(req.getLogType())
      val generator = generatorMap.get(typeConfig.getParser())

      val archiveFilePath = Paths.get(uploadedDir, req.getContainerId(), req.getArchiveId(), req.getArchiveFileName()).toString();

      val targetFile = File.createTempFile("temp", "log", new File(embulkSetting.getTemporaryPath()));
      try (InputStream is = ArchiveUtil.getTargetStream(archiveFilePath, req.getFilePath())) {
        Files.copy(is, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
      }

      val indexName = IndexBuilder.getFullIndex(req.getContainerId(), req.getLogType(), req.getArchiveId(), req.getFilePath());

      // preprocess
      targetFile = generator.preprocess(targetFile);

      // generate config file
      String configPath = generator.generate(req.getLogType(), targetFile.toPath(), indexName, req.getExtra());
      logger.info("generated yaml: " + configPath);
      ConfigLoader loader = embulkEmbed.newConfigLoader();
      ConfigSource config = loader.fromYamlFile(new File(configPath));

      // guess
      if (typeConfig.isDoGuess()) {
        ConfigDiff diff = embulkEmbed.guess(config);
        config.merge(diff);
      }

      logger.info(config);

      // run
      logger.info("embulk running...");
      ExecutionResult result = embulkEmbed.run(config);
      logger.info("embulk done.");
      RegisterResponse res = new RegisterResponse();
      res.setErrorCount(result.getIgnoredExceptions().size());
      if (result.getIgnoredExceptions().isEmpty()) {
        res.setErrorMessage("");
      } else {
        res.setErrorMessage(result.getIgnoredExceptions().size() + " errors. first message: " + result.getIgnoredExceptions().get(0).getMessage());
      }
      return Observable.just(res);
    } catch (Throwable e) {
      logger.error(e);
      return Observable.error(e);
    }

  }
  */
}
