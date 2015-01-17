package iobjects.entity;

import java.io.IOException;
import java.lang.reflect.*;
import java.util.*;

import iobjects.*;
import iobjects.remote.*;

/**
 * Representa a classe base de todas as classes que mant�m atributos de
 * entidades da aplica��o.
 */
public class EntityInfo implements Serializable, Comparator {

  private EntityLookupValueList lookupValueList = new EntityLookupValueList();

  static public void main(String[] args) {
    /*
    try {
      EmpresaInfo empresaInfo = new EmpresaInfo();
      empresaInfo.setNome("Lepra da peste");
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      Serializer.serialize(empresaInfo, bos);
      
      ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
      EmpresaInfo desEmpresaInfo = (EmpresaInfo)Serializer.deserialize(bis);
      System.out.println(desEmpresaInfo.getNome());
    } catch (Exception e){
      e.printStackTrace();
    }
   */
  }

  /**
   * Construtor padr�o.
   */
  public EntityInfo() {
  }

  /**
   * Deserializa as propriedades a partir de 'reader'.
   * @param reader
   * @throws IOException
   */
  public int compare(Object o1, Object o2) {
    return ((Method)o1).getName().compareTo(((Method)o2).getName());
  }

  public void deserialize(SerializeReader reader) throws IOException {
    try {
      // nossos m�todos
      Method[] methods = getClass().getDeclaredMethods();
      // garante ordem alfab�tica
      Arrays.sort(methods, this);
      // loop nos m�todos
      for (int i=0; i<methods.length; i++) {
        // m�todo da vez
        Method method = methods[i];
        // se n�o � um get...continua
        if (!method.getName().startsWith("get") || (method.getParameterTypes().length != 0))
          continue;
        // busca o m�todo set
        String setName = "s" + method.getName().substring(1);
        Method set     = null;
        for (int w=0; w<methods.length; w++) {
          if (methods[w].getName().equals(setName)) {
            set = methods[w];
            break;
          } // if
        } // for
        // se n�o achamos o m�todo...exce��o
        if (set == null)
          throw new ExtendedException(getClass().getName(), "deserialize", "M�todo n�o encontrado: " + setName + ".");
        // define o valor pelo m�todo set
        Object obj = reader.readObject();
        if (obj instanceof Integer)
          set.invoke(this, ((Integer)obj).intValue());
        else if (obj instanceof Boolean)
          set.invoke(this, ((Boolean)obj).booleanValue());
        else if (obj instanceof Double)
          set.invoke(this, ((Double)obj).doubleValue());
        else
          set.invoke(this, obj);
      } // for
      // nossos lookups
      int lookupSize = reader.readInt();
      for (int i=0; i<lookupSize; i++) {
        String lookupName = reader.readString();
        int displayFieldValuesSize = reader.readInt();
        String[] displayFieldValues = new String[displayFieldValuesSize];
        for (int w=0; w<displayFieldValues.length; w++)
          displayFieldValues[w] = reader.readString();
        EntityLookup lookup = new EntityLookup();
        lookup.setName(lookupName);
        EntityLookupValue lookupValue = new EntityLookupValue(lookup, displayFieldValues);
        lookupValueList().add(lookupValue);
      } // for
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    } // try-catch
  }

  /**
   * Retorna o m�todo de nome indicado por 'methodName' ignorando o caso de
   * mai�sculas e min�sculas.
   * @param methodName String Nome do m�todo que se deseja retornar.
   * @return Method Retorna o m�todo de nome indicado por 'methodName' ignorando
   *         o caso de mai�sculas e min�sculas.
   */
  private Method getMethodByName(String methodName) {
    // nossa lista de m�todos
    Method[] methods = getClass().getMethods();
    // procura pelo m�todo de leitura da propriedade
    Method result;
    for (int i=0; i<methods.length; i++) {
      // m�todo da vez
      result = methods[i];
      // se tem o mesmo nome...retorna-o
      if (result.getName().equalsIgnoreCase(methodName))
        return result;
    } // for
    // se chegou at� aqui...n�o achamos nada
    return null;
  }

  /**
   * Retorna o valor da propriedade de nome 'propertyName'.
   * @param propertyName String Nome da propriedade cujo valor se deseja obter.
   * @throws Exception Em caso de exce��o na tentativa de localizar o m�todo de
   *         leitura da propriedade ou de ler o valor da propriedade.
   * @return Object Retorna o valor da propriedade de nome 'propertyName'.
   */
  public Object getPropertyValue(String propertyName) throws Exception {
    // pega o m�todo de leitura da propriedade informada
    Method method = getMethodByName("get" + propertyName);
    // se n�o temos o m�todo...exce��o
    if (method == null)
      throw new ExtendedException(getClass().getName(), "getPropertyValue", "Propriedade n�o encontrada: " + propertyName + ".");
    // retorna o valor da propriedade
    return method.invoke(this, null);
  }

  /**
   * Retorna o EntityLookupValueList que mant�m os valores dos lookups referentes
   * ao EntityInfo.
   * @return EntityLookupValueList que mant�m os valores dos lookups referentes
   * ao EntityInfo.
   */
  public EntityLookupValueList lookupValueList() {
    return lookupValueList;
  }

  /**
   * Serializa as propriedades em 'writer'.
   * @param writer
   * @throws IOException
   */
  public void serialize(SerializeWriter writer) throws IOException {
    try {
      // nossos m�todos
      Method[] methods = getClass().getDeclaredMethods();
      // garante ordem alfab�tica
      Arrays.sort(methods, this);
      // loop nos m�todos
      Object[] args = new Object[0];
      for (int i=0; i<methods.length; i++) {
        // m�todo da vez
        Method method = methods[i];
        // se n�o � um get...continua
        if (!method.getName().startsWith("get") || (method.getParameterTypes().length != 0))
          continue;
        // adiciona o valor do m�todo
        writer.writeObject(method.invoke(this, args));
      } // for
      // nossos lookups
      writer.writeInt(lookupValueList.size());
      for (int i=0; i<lookupValueList.size(); i++) {
        EntityLookupValue lookupValue = lookupValueList.get(i);
        writer.writeString(lookupValue.getLookup().getName());
        writer.writeInt(lookupValue.getDisplayFieldValues().length);
        for (int w=0; w<lookupValue.getDisplayFieldValues().length; w++)
          writer.writeString(lookupValue.getDisplayFieldValues()[w]);
      } // for
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    } // try-catch
  }

  /**
   * Define o valor da propriedade de nome 'propertyName' para 'value'.
   * @param propertyName String Nome da propriedade cujo valor se deja definir.
   * @param value Object Valor que se deseja definir � propriedade.
   * @throws Exception Em caso de exce��o na tentativa de localizar o m�todo de
   *         escrita da propriedade ou de definir o valor da propriedade.
   */
  public void setPropertyValue(String propertyName,
                               Object value) throws Exception {
    // pega o m�todo de escrita da propriedade informada
    Method method = getMethodByName("set" + propertyName);
    // se n�o temos o m�todo...exce��o
    if (method == null)
      throw new ExtendedException(getClass().getName(), "setPropertyValue", "Propriedade n�o encontrada: " + propertyName + ".");
    // define o valor da propriedade
    method.invoke(this, new Object[]{value});
  }

}
