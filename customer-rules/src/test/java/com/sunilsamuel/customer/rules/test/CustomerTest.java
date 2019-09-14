package com.sunilsamuel.customer.rules.test;

import java.util.ArrayList;
import java.util.List;

import org.drools.core.common.DefaultFactHandle;
import org.junit.Assert;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.command.BatchExecutionCommand;
import org.kie.api.command.Command;
import org.kie.api.runtime.ExecutionResults;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.StatelessKieSession;
import org.kie.internal.command.CommandFactory;

import com.sunilsamuel.simple.brms.model.Employee;


public class CustomerTest {
	KieContainer kieContainer = KieServices.Factory.get().getKieClasspathContainer();

	@Test
	public void entry() {
		Employee employee = new Employee("Sunil Samuel", 100, "Entry", "A", true);
		Employee emp = processEmployee(employee);
		Assert.assertEquals(20.45d, emp.getCustomerAdditive(),0);
		System.out.println(emp);
	}
	
	@Test
	public void junior() {
		Employee employee = new Employee("Sunil Samuel", 100, "Junior", "A", true);
		Employee emp = processEmployee(employee);
		Assert.assertEquals(40.45d, emp.getCustomerAdditive(),0);
		System.out.println(emp);
	}
	
	@Test
	public void senior() {
		Employee employee = new Employee("Sunil Samuel", 100, "Senior", "A", true);
		Employee emp = processEmployee(employee);
		Assert.assertEquals(60.45d, emp.getCustomerAdditive(),0);
		System.out.println(emp);
	}


	private Employee processEmployee(Employee emp) {
		List<Command<?>> commands;

		/**
		 * Create the commands to run the rules and get the query.
		 */
		commands = new ArrayList<Command<?>>();

		commands.add(CommandFactory.newInsert(emp, "Employee"));
		commands.add(CommandFactory.newStartProcess("com.sunilsamuel.customer.rules.process"));
		commands.add(CommandFactory.newFireAllRules());
		StatelessKieSession ksession = kieContainer.newStatelessKieSession("customerStatelessSession");

		BatchExecutionCommand batch = CommandFactory.newBatchExecution(commands);
		ExecutionResults results = ksession.execute(batch);
		DefaultFactHandle rval = (DefaultFactHandle) results.getFactHandle("Employee");
		return (Employee) rval.getObject();
	}

}
