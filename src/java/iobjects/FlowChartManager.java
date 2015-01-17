package iobjects;

import iobjects.*;
import iobjects.util.*;
import iobjects.xml.ui.*;
import iobjects.ui.*;

/**
 * Classe responsável pelo gerenciamento dos arquivos de conexão com o banco de
 * dados.
 */
public class FlowChartManager {

  /**
   * Define a extensão dos arquivos de configuração de fluxograma.
   */
  static public final String FLOW_CHART_FILE_EXTENSION = ".flow";

  static private FlowChartManager flowChartManager = null;

  private FlowChartFiles flowChartFiles     = new FlowChartFiles();
  private String         flowChartFilesPath = "";

  /**
   * construtor padrão
   * @param flowChartFilesPath String Caminho onde se encontram os arquivos
   *        de fluxograma da aplicação.
   */
  private FlowChartManager(String flowChartFilesPath) {
    // nossos valores
    this.flowChartFilesPath = flowChartFilesPath;
    try {
      // carrega as conexões
      loadFlowChartFiles();
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    } // try-catch
  }

  /**
   * Retorna a lista de conexões.
   * @return Retorna a lista de conexões.
   */
  public FlowChartFiles flowChartFiles() {
    return flowChartFiles;
  }

  /**
   * Retorna um FlowChart obtido através dos parâmetros encontrados no
   * arquivo de conexão referenciado por 'flowChartName'.
   * @param flowChartName String Nome da conexão.
   * @throws Exception Em caso de exceção na tentativa de inicar a conexão.
   * @return FlowChart Retorna um FlowChart obtido através dos parâmetros
   *         encontrados no arquivo de conexão referenciado por 'flowChartName'.
   */
  public FlowChart getFlowChart(String flowChartName) throws Exception {
    // localiza o arquivo de conexão
    FlowChartFile flowChartFile = flowChartFiles.get(flowChartName);
    // se não achou o arquivo...exceção
    if (flowChartFile == null)
      throw new ExtendedException(getClass().getName(), "getFlowChart", "Fluxograma não encontrado '" + flowChartName + "'.");
    // nosso resultado
    FlowChart result = new FlowChart();
    result.setCaption(flowChartFile.information().getCaption());
    result.setDescription(flowChartFile.information().getDescription());
    result.setName(flowChartName);
    // loop nos Actions em FLOW
    for (int i=0; i<flowChartFile.flow().size(); i++) {
      // Action da vez
      FlowChartFile.Action flowAction = flowChartFile.flow().get(i);
      // procura o Action na aplicação
      Action action = Facade.applicationActionList().get(flowAction.getName(), false);
      // adiciona no resultado
      result.addItem(action,
                     flowAction.getCaption(),
                     flowAction.getDescription(),
                     flowAction.getKind());
    } // for
    // loop nos Actions em SEEALSO
    String[] seeAlso = flowChartFile.seeAlso();
    for (int i=0; i<seeAlso.length; i++) {
      // procura o Action na aplicação
      Action action = Facade.applicationActionList().get(seeAlso[i], false);
      // adiciona no resultado
      result.addSeeAlso(action);
    } // for
    // retorna
    return result;
  }

  /**
   * Retorna a instância de FlowChartManager para a aplicação.
   * @param flowChartFilesPath String Caminho onde se encontram os arquivos
   *        de conexão com banco de dados.
   * @return Retorna a instância de FlowChartManager para a aplicação.
   */
  static public FlowChartManager getInstance(String flowChartFilesPath) {
    if (flowChartManager == null)
      flowChartManager = new FlowChartManager(flowChartFilesPath);
    return flowChartManager;
  }

  /**
   * Carrega os arquivos de conexões de banco de dados.
   * @throws Exception Em caso de exceção na tentativa de acesso aos arquivos
   *                   de conexão com o banco de dados.
   */
  private void loadFlowChartFiles() throws Exception {
    // pega a lista de arquivos de configuração de conexões
    String[] fileExtensions = {FLOW_CHART_FILE_EXTENSION};
    String[] fileNames = FileTools.getFileNames(flowChartFilesPath, fileExtensions, false);
    // limpa as conexões atuais
    flowChartFiles.clear();
    // loop nos arquivos para carregá-los
    for (int i=0; i<fileNames.length; i++) {
      // arquivo da vez
      String fileName = fileNames[i];
      // carrega o arquivo
      FlowChartFile flowChartFile = new FlowChartFile(flowChartFilesPath + fileName);
      // adiciona a conexão
      flowChartFiles.add(flowChartFile, fileName.substring(0, fileName.indexOf('.')));
    } // for
  }

}
