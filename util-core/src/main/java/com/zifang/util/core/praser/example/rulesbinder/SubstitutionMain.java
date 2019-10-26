
package com.zifang.util.core.praser.example.rulesbinder;

import com.zifang.util.core.praser.example.simpletest.ExampleMain;
import com.zifang.util.core.praser.example.rulesbinder.module.EmployeeModule;
import com.zifang.util.core.praser.example.rulesbinder.pojo.Address;
import com.zifang.util.core.praser.example.rulesbinder.pojo.Employee;
import org.apache.commons.digester3.Digester;
import org.apache.commons.digester3.Substitutor;
import org.apache.commons.digester3.binder.DigesterLoader;
import org.apache.commons.digester3.substitution.MultiVariableExpander;
import org.apache.commons.digester3.substitution.VariableSubstitutor;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 
 * @author http://www.cnblogs.com/chenpi/
 * @version 2017年6月5日
 */
public class SubstitutionMain
{
    private static DigesterLoader dl = DigesterLoader.newLoader(new EmployeeModule())
        .setNamespaceAware(false);

    public static void main(String[] args)
    {

        try
        {
            // set up the variables the input xml can reference
            Map<String, Object> vars = new HashMap<String, Object>();
            vars.put("user.name", "me");
            vars.put("type", "boss");

            // map ${varname} to the entries in the var map
            MultiVariableExpander expander = new MultiVariableExpander();
            expander.addSource("$", vars);

            // allow expansion in both xml attributes and element text
            Substitutor substitutor = new VariableSubstitutor(expander);

            Digester digester = dl.newDigester();
            digester.setSubstitutor(substitutor);

            Employee employee = digester
                .parse(ExampleMain.class.getClassLoader().getResourceAsStream("employee$.xml"));

            System.out.print(employee.getFirstName() + " ");
            System.out.print(employee.getLastName() + ", ");
            for (Address a : employee.getAddressList())
            {
                System.out.print(a.getType() + ", ");
                System.out.print(a.getCity() + ", ");
                System.out.println(a.getState());
            }

        }
        catch (IOException e)
        {

            e.printStackTrace();
        }
        catch (SAXException e)
        {

            e.printStackTrace();
        }
    }
}
