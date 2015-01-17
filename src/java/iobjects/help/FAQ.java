package iobjects.help;

/**
 * Representa uma pergunta comumente realizada no sistema de ajuda da aplica��o.
 * @since 2006
 */
public class FAQ {

  private String  answer               = "";
  private String  answerFileName       = "";
  private String  question             = "";
  private String  name                 = "";
  private FAQList seeAlsoFAQList       = new FAQList();
  private String  module               = "";
  private FAQ     parentFAQ            = null;
  private String  presentationFileName = "";

  /**
   * Construtor estendido.
   * @param name String Nome da FAQ.
   * @param module String M�dulo da FAQ.
   * @param question String Pergunta da FAQ.
   * @param answer String Resposta da FAQ. Pode ser utilizada marca��o HTML para
   *                      incrementar o visual.
   */
  public FAQ(String name,
             String module,
             String question,
             String answer) {
    this(name, module, question, answer, "", "");
  }

  /**
   * Construtor padr�o.
   * @param name String Nome da FAQ.
   * @param module String M�dulo da FAQ.
   * @param question String Pergunta da FAQ.
   * @param answer String Resposta da FAQ. Pode ser utilizada marca��o HTML para
   *                      incrementar o visual.
   * @param answerFileName Alternativamente � resposta, pode ser informado o nome
   *                       do arquivo texto que a cont�m. Podem ser utilizados
   *                       arquivos txt, html, etc.
   *                       <b>Os arquivos devem estar localizados na extens�o
   *                       no caminho definido em Facade.FAQS_PATH.</b>
   * @param presentationFileName String Nome do arquivo de apresenta��o para a
   *                             FAQ. Podem ser utilizados arquivos html,
   *                             flash, ppt, pps, etc. <b>Os arquivos devem estar
   *                             localizados na extens�o no caminho definido em
   *                             Facade.FAQS_PATH. O usu�rio deve possuir
   *                             o programa visualizador instalado em sua m�quina.</b>
   */
  public FAQ(String name,
             String module,
             String question,
             String answer,
             String answerFileName,
             String presentationFileName) {
    this.name = name;
    this.module = module;
    this.question = question;
    this.answer = answer;
    this.answerFileName = answerFileName;
    this.presentationFileName = presentationFileName;
  }

  /**
   * Adiciona 'faq' � lista de FAQ's relacionadas.
   * @param faq FAQ para ser adicionada a SeeAlsoFaqList.
   */
  public void addSeeAlsoFAQ(FAQ faq) {
    // adiciona a nossa lista
    seeAlsoFAQList.add(faq);
    // associa-nos como seu pai
    faq.setParentFAQ(this);
  }

  /**
   * Retorna a resposta da FAQ.
   * @return String Retorna a resposta da FAQ.
   */
  public String getAnswer() {
    return answer;
  }

  /**
   * Retorna o nome do arquivo de resposta da FAQ.
   * @return String Retorna o nome do arquivo de resposta da FAQ.
   */
  public String getAnswerFileName() {
    return answerFileName;
  }

  /**
   * Retorna o m�dulo da FAQ.
   * @return String Retorna o m�dulo da FAQ.
   */
  public String getModule() {
    return module;
  }

  /**
   * Retorna o nome da FAQ.
   * @return String Retorna o nome da FAQ.
   */
  public String getName() {
    return name;
  }

  /**
   * Retorna a FAQ pai para o caso de esta FAQ ser aninhada.
   * @return FAQ Retorna a FAQ pai para o caso de esta FAQ ser aninhada.
   */
  public FAQ getParentFAQ() {
    return parentFAQ;
  }

  /**
   * Retorna o nome do arquivo de apresenta��o da FAQ.
   * @return String
   */
  public String getPresentationFileName() {
    return presentationFileName;
  }

  /**
   * Retorna a pergunta da FAQ.
   * @return String Retorna a pergunta da FAQ.
   */
  public String getQuestion() {
    return question;
  }

  /**
   * Retorna a lista de FAQ's relacionadas.
   * @return FAQList Retorna a lista de FAQ's relacionadas.
   */
  public FAQList seeAlsoFAQList() {
    return seeAlsoFAQList;
  }

  public void setAnswer(String answer) {
    this.answer = answer;
  }

  public void setAnswerFileName(String answerFileName) {
    this.answerFileName = answerFileName;
  }

  /**
   * Define a FAQ pai para o caso de esta FAQ ser aninhada.
   * @param parentFAQ FAQ pai para o caso de esta FAQ ser aninhada.
   */
  public void setParentFAQ(FAQ parentFAQ) {
    this.parentFAQ = parentFAQ;
  }

}
