package com.javexpress.gwt.library.rebind;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JParameter;
import com.google.gwt.core.ext.typeinfo.JParameterizedType;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;
import com.javexpress.common.model.item.IJSONSerializable;
import com.javexpress.gwt.library.ui.js.AsyncRequestBuilder;
import com.javexpress.gwt.library.ui.js.JsonMap;

public class JexpRemoteServiceProxyGenerator extends Generator {

	@Override
	public String generate(TreeLogger logger, GeneratorContext context, String typeName) throws UnableToCompleteException {
		TypeOracle typeOracle = context.getTypeOracle();
		assert (typeOracle != null);

		JClassType serverServiceIntf = typeOracle.findType(typeName);
		if (serverServiceIntf == null) {
			logger.log(TreeLogger.ERROR, "Unable to find metadata for type '" + typeName + "'", null);
			throw new UnableToCompleteException();
		}
		RemoteServiceRelativePath path = serverServiceIntf.getAnnotation(RemoteServiceRelativePath.class);
		if (path == null) {
			logger.log(TreeLogger.ERROR, "Unable to find service path (@RemoteServiceRelativePath) for type '" + typeName + "'", null);
			throw new UnableToCompleteException();
		}

		final String asyncName = serverServiceIntf.getSimpleSourceName() + "Async";
		JClassType clientServiceIntf = typeOracle.findType(asyncName);
		if (serverServiceIntf == null) {
			logger.log(TreeLogger.ERROR, "Unable to find metadata for type '" + asyncName + "'", null);
			throw new UnableToCompleteException();
		}

		final String packageName = serverServiceIntf.getPackage().getName();
		final String simpleName = serverServiceIntf.getSimpleSourceName() + "AsyncImpl";
		ClassSourceFileComposerFactory composerFactory = new ClassSourceFileComposerFactory(packageName, simpleName);
		PrintWriter printWriter = context.tryCreate(logger, packageName, simpleName);
		SourceWriter sourceWriter = composerFactory.createSourceWriter(context, printWriter);
		composerFactory.addImport(GWT.class.getCanonicalName());
		composerFactory.addImport(Request.class.getCanonicalName());
		composerFactory.addImport(RequestBuilder.class.getCanonicalName());
		composerFactory.addImport(RequestCallback.class.getCanonicalName());
		composerFactory.addImport(Response.class.getCanonicalName());
		composerFactory.addImport(JSONValue.class.getCanonicalName());
		composerFactory.addImport(JSONParser.class.getCanonicalName());
		composerFactory.addImport(IJSONSerializable.class.getCanonicalName());
		composerFactory.addImport(JsonMap.class.getCanonicalName());
		composerFactory.addImport(AsyncRequestBuilder.class.getCanonicalName());
		composerFactory.addImport(clientServiceIntf.getQualifiedBinaryName());
		composerFactory.addImplementedInterface(clientServiceIntf.getQualifiedBinaryName());
		List<String> willBeImported = new ArrayList<String>();
		List<JClassType> serdesList = new ArrayList<JClassType>();
		for (JMethod method : clientServiceIntf.getMethods()) {
			sourceWriter.println("@Override");
			sourceWriter.print("public void " + method.getName() + "(");
			JParameter[] params = method.getParameters();
			if (params != null) {
				boolean hasParamsBefore = false;
				for (JParameter param : params) {
					if (hasParamsBefore)
						sourceWriter.print(",");
					sourceWriter.print("final " + param.getType().getQualifiedBinaryName() + " " + param.getName());
					hasParamsBefore = true;
					if (!willBeImported.contains(param.getType().getQualifiedBinaryName()))
						willBeImported.add(param.getType().getQualifiedBinaryName());
				}
			}
			sourceWriter.print(")");
			JClassType[] throwns = method.getThrows();
			if (throwns != null && throwns.length > 0) {
				sourceWriter.print(" throws ");
				boolean hasThrowsBefore = false;
				for (JClassType thrown : throwns) {
					if (hasThrowsBefore)
						sourceWriter.print(",");
					sourceWriter.print(thrown.getQualifiedBinaryName());
					hasThrowsBefore = true;
					if (!willBeImported.contains(thrown.getQualifiedBinaryName()))
						willBeImported.add(thrown.getQualifiedBinaryName());
				}
			}
			sourceWriter.println("{");
			createRequestor(logger, sourceWriter, method, path.value(), serdesList);
			sourceWriter.println("}");
			// method body ends
		}
		for (String imp : willBeImported)
			composerFactory.addImport(imp);
		sourceWriter.commit(logger);
		return packageName + "." + simpleName;
	}

	private void createRequestor(TreeLogger logger, SourceWriter sourceWriter, JMethod method, String path, List<JClassType> serdesList) throws UnableToCompleteException {
		boolean noreturn = method.getReturnType().getQualifiedBinaryName().endsWith(".Void");
		sourceWriter.println("final JsonMap _params_ = new JsonMap();");
		JParameter[] params = method.getParameters();
		sourceWriter.println("AsyncRequestBuilder rb = new AsyncRequestBuilder(" + (params != null && params.length > 1 ? "RequestBuilder.POST" : "RequestBuilder.GET") + ", GWT.getModuleBaseURL() + \"" + path + "." + method.getName() + "\");");
		sourceWriter.println("rb.setHeader(\"Content-Type\", \"application/json\");");
		if (params != null && params.length > 1) {
			for (int i = 0; i < params.length - 1; i++) {
				sourceWriter.println("if (" + params[i].getName() + " instanceof " + IJSONSerializable.class.getSimpleName() + ")");
				sourceWriter.println("_params_.put(\"" + params[i].getName() + "\", toJsonObject(" + params[i] + "));");
				sourceWriter.println("else if (java.util.Collection.class.isAssignableFrom(" + params[i].getName() + ".getClass()))");
				sourceWriter.println("_params_.put(\"" + params[i].getName() + "\", toJsonArray(" + params[i] + "));");
				sourceWriter.println("else _params_.set(\"" + params[i].getName() + "\"," + params[i].getName() + ");");
			}
			sourceWriter.println("rb.setRequestData(_params_.toString());");
		}
		sourceWriter.println("rb.setCallback(new RequestCallback() {");
		sourceWriter.println("@Override");
		sourceWriter.println("public void onResponseReceived(Request request, Response response) {");
		if (noreturn) {
			sourceWriter.println("callback.onSuccess(null);");
		} else {
			sourceWriter.println("if (JsUtil.isEmpty(response.getText())){");
			sourceWriter.println("callback.onSuccess(null);");
			sourceWriter.println("return;");
			sourceWriter.println("}");
			JClassType returnClassType = method.getReturnType().isClass();
			sourceWriter.println("JSONValue json = JSONParser.parseStrict(response.getText());");
			if (returnClassType != null) {
				if (!serdesList.contains(returnClassType))
					serdesList.add(returnClassType);
				sourceWriter.println(returnClassType.getQualifiedBinaryName() + " result = new " + returnClassType.getQualifiedBinaryName() + "();");
				sourceWriter.println("fillObjectFrom(result, json.isObject());");
				sourceWriter.println("callback.onSuccess(result);");
			} else {
				try {
					Class returnClass = Class.forName(method.getReturnType().getQualifiedBinaryName());
					if (Collection.class.isAssignableFrom(returnClass)) {
						JParameterizedType paramType = method.getReturnType().isParameterized();
						if (paramType == null) {
							logger.log(TreeLogger.ERROR, "Unable to find return type parameter for '" + method.getName() + "'", null);
							throw new UnableToCompleteException();
						}
						sourceWriter.print(method.getReturnType().getParameterizedQualifiedSourceName() + " result = ");
						if (Set.class.isAssignableFrom(returnClass))
							sourceWriter.println("new java.util.HashSet<" + paramType.getQualifiedBinaryName() + ">();");
						else
							sourceWriter.println("new java.util.ArrayList<" + paramType.getQualifiedBinaryName() + ">();");
						sourceWriter.println("fillListFrom(result, json.isArray);");
						sourceWriter.println("callback.onSuccess(result);");
					}
				} catch (ClassNotFoundException e) {
					logger.log(TreeLogger.ERROR, "Unable to find class '" + method.getReturnType().getQualifiedBinaryName() + "'", null);
					throw new UnableToCompleteException();
				}
			}
		}
		sourceWriter.println("}");
		sourceWriter.println("@Override");
		sourceWriter.println("public void onError(Request request, Throwable exception) {");
		sourceWriter.println("callback.onFailure(exception);");
		sourceWriter.println("}");
		sourceWriter.println("});");
		sourceWriter.println("rb.send();");
	}

}