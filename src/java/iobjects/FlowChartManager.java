package iobjects;

import iobjects.*;
import iobjects.util.*;
import iobjects.xml.ui.*;
import iobjects.ui.*;

/**
 * Classe respons�vel pelo gerenciamento dos arquivos de conex�o com o banco de
 * dados.
 */
public class FlowChartManager {

  /**
   * Define a extens�o dos arquivos de configura��o de fluxograma.
   */
  static public final String FLOW_CHART_FILE_EXTENSION = ".flow";

  static private FlowChartManager flowChartManager = null;

  private FlowChartFiles flowChartFiles     = new FlowChartFiles();
  private String         flowChartFilesPath = "";

  /**
   * construtor padr�o
   * @param flowChartFilesPath String Caminho onde se encontram os arquivos
   *        de fluxograma da aplica��o.
   */
  private FlowChartManager(String flowChartFilesPath) {
    // nossos valores
    this.flowChartFilesPath = flowChartFilesPath;
    try {
      // carrega as conex�es
      loadFlowChartFiles();
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    } // try-catch
  }

  /**
   * Retorna a lista de conex�es.
   * @return Retorna a lista de conex�es.
   */
  public FlowChartFiles flowChartFiles() {
    return flowChartFiles;
  }

  /**
   * Retorna um FlowChart obtido atrav�s dos par�metros encontrados no
   * arquivo de conex�o referenciado por 'flowChartName'.
   * @param flowChartName String Nome da conex�o.
   * @throws Exception Em caso de exce��o na tentativa de inicar a conex�o.
   * @return FlowChart Retorna um FlowChart obtido atrav�s dos par�metros
   *         encontrados no arquivo de conex�o referenciado por 'flowChartName'.
   */
  public FlowChart getFlowChart(String flowChartName) throws Exception {
    // localiza o arquivo de conex�o
    FlowChartFile flowChartFile = flowChartFiles.get(flowChartName);
    // se n�o achou o arquivo...exce��o
    if (flowChartFile == null)
      throw new ExtendedException(getClass().getName(), "getFlowChart", "Fluxograma n�o encontrado '" + flowChartName + "'.");
    // nosso resultado
    FlowChart result = new FlowChart();
    result.setCaption(flowChartFile.information().getCaption());
    result.setDescription(flowChartFile.information().getDescription());
    result.setName(flowChartName);
    // loop nos Actions em FLOW
    for (int i=0; i<flowChartFile.flow().size(); i++) {
      // Action da vez
      FlowChartFile.Action flowAction = flowChartFile.flow().get(i);
      // procura o Action na aplica��o
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
      // procura o Action na aplica��o
      Action action = Facade.applicationActionList().get(seeAlso[i], false);
      // adiciona no resultado
      result.addSeeAlso(action);
    } // for
    // retorna
    return result;
  }

  /**
   * Retorna a inst�ncia de FlowChartManager para a aplica��o.
   * @param flowChartFilesPath String Caminho onde se encontram os arquivos
   *        de conex�o com banco de dados.
   * @return Retorna a inst�ncia de FlowChartManager para a aplica��o.
   */
  static public FlowChartManager getInstance(String flowChartFilesPath) {
    if (flowChartManager == null)
      flowChartManager = new FlowChartManager(flowChartFilesPath);
    return flowChartManager;
  }

  /**
   * Carrega os arquivos de conex�es de banco de dados.
   * @throws Exception Em caso de exce��o na tentativa de acesso aos arquivos
   *                   de conex�o com o banco de dados.
   */
  private void loadFlowChartFiles() throws Exception {
    // pega a lista de arquivos de configura��o de conex�es
    String[] fileExtensions = {FLOW_CHART_FILE_EXTENSION};
    String[] fileNames = FileTools.getFileNames(flowChartFilesPath, fileExtensions, false);
    // limpa as conex�es atuais
    flowChartFiles.clear();
    // loop nos arquivos para carreg�-los
    for (int i=0; i<fileNames.length; i++) {
      // arquivo da vez
      String fileName = fileNames[i];
      // carrega o arquivo
      FlowChartFile flowChartFile = new FlowChartFile(flowChartFilesPath + fileName);
      // adiciona a conex�o
      flowChartFiles.add(flowChartFile, fileName.substring(0, fileName.indexOf('.')));
    } // for
  }

}
