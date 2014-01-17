package tern.server.protocol.angular;

import org.junit.Assert;
import org.junit.Test;

import tern.TernException;
import tern.angular.AngularType;
import tern.angular.protocol.definition.TernAngularDefinitionQuery;
import tern.server.protocol.MockTernDefinitionCollector;
import tern.server.protocol.TernDoc;

/**
 * Tests with tern angular controller definition.
 * 
 */
public abstract class AbstractAngularControllerDefinitionTest extends
		AbstractTernServerAngularTest {

	@Test
	public void definitionWithModuleControllersAndBadModule()
			throws TernException {
		TernDoc doc = createDocForDefinitionModuleControllers(null);
		MockTernDefinitionCollector collector = new MockTernDefinitionCollector();
		server.request(doc, collector);

		Assert.assertNull(collector.getFile());
	}

	@Test
	public void definitionWithModuleControllersAndModule() throws TernException {
		TernDoc doc = createDocForDefinitionModuleControllers("phonecatControllers");
		MockTernDefinitionCollector collector = new MockTernDefinitionCollector();
		server.request(doc, collector);

		Assert.assertNotNull(collector.getFile());
		Assert.assertEquals("myfile.js", collector.getFile());
	}

	private TernDoc createDocForDefinitionModuleControllers(String module) {
		String name = "myfile.js";

		String text = "var phonecatControllers = angular.module('phonecatControllers', [])";
		text += "\nphonecatControllers.controller('PhoneListCtrl', ['$scope', 'Phone',";
		text += "\nfunction($scope, Phone) {";
		text += "\n$scope.phones = Phone.query();";
		text += "\n$scope.orderProp = 'age';";
		text += "\n}]);";

		text += "\nphonecatControllers.controller('PhoneDetailCtrl', ['$scope', '$routeParams', 'Phone',";
		text += "\nfunction($scope, $routeParams, Phone) {";
		text += "\n$scope.phone = Phone.get({phoneId: $routeParams.phoneId}, function(phone) {";
		text += "\n$scope.mainImageUrl = phone.images[0];";
		text += "\n});";

		text += "\n$scope.setImage = function(imageUrl) {";
		text += "\n$scope.mainImageUrl = imageUrl;";
		text += "\n}";
		text += "\n}]);";

		TernDoc doc = new TernDoc();
		doc.addFile(name, text, null);

		TernAngularDefinitionQuery query = new TernAngularDefinitionQuery(
				AngularType.controller);
		query.getScope().setModule(module);
		query.addFile("myfile.js");
		query.setExpression("PhoneDetailCtrl");

		doc.setQuery(query);
		return doc;
	}

	@Test
	public void definitionWithGlobalControllers() throws TernException {
		TernDoc doc = createDocForGlobalControllers();
		MockTernDefinitionCollector collector = new MockTernDefinitionCollector();
		server.request(doc, collector);

		Assert.assertNotNull(collector.getFile());
		Assert.assertEquals("myfile.js", collector.getFile());
	}

	private TernDoc createDocForGlobalControllers() {
		String name = "myfile.js";
		String text = "function TodoCtrl($scope) {};";
		text += "\nfunction SomeCtrl($scope) {};";

		TernDoc doc = new TernDoc();
		doc.addFile(name, text, null);

		TernAngularDefinitionQuery query = new TernAngularDefinitionQuery(
				AngularType.controller);
		query.addFile("myfile.js");
		query.setExpression("TodoCtrl");

		doc.setQuery(query);
		return doc;
	}

	@Test
	public void definitionWithGlobalControllersCheckFiles()
			throws TernException {

		server.addFile("myfile.js", "function TodoCtrl($scope) {};");
		server.addFile("myfile2.js", "function TodoCtrl($scope) {};");

		TernDoc doc = createDocForGlobalControllersCheckFiles();
		MockTernDefinitionCollector collector = new MockTernDefinitionCollector();
		server.request(doc, collector);

		Assert.assertNotNull(collector.getFile());
		Assert.assertEquals("myfile.js", collector.getFile());

	}

	private TernDoc createDocForGlobalControllersCheckFiles() {

		TernDoc doc = new TernDoc();

		TernAngularDefinitionQuery query = new TernAngularDefinitionQuery(
				AngularType.controller);
		query.addFile("myfile.js");
		query.setExpression("TodoCtrl");

		doc.setQuery(query);
		return doc;
	}
}
