/*******************************************************************************
 * Copyright (c) 2011 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtext.xbase.impl;

import static com.google.common.collect.Lists.*;
import static java.util.Collections.*;

import java.util.List;

import org.eclipse.xtext.common.types.JvmIdentifiableElement;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.xbase.XAbstractFeatureCall;
import org.eclipse.xtext.xbase.XExpression;
import org.eclipse.xtext.xbase.resource.LinkingAssumptions;

import com.google.inject.Inject;

/**
 * @author Sven Efftinge - Initial contribution and API
 * @author Sebastian Zarnekow - Support for linking assumptions
 */
public class FeatureCallToJavaMapping {
	
	@Inject
	private LinkingAssumptions linkingAssumptions;
	
	protected boolean isStaticJavaFeature(JvmIdentifiableElement feature) {
		if (feature instanceof JvmOperation) {
			return ((JvmOperation) feature).isStatic();
		}
		return false;
	}
	
	public XExpression getActualReceiver(XAbstractFeatureCall call) {
		return getActualReceiver(call, getFeature(call), getImplicitReceiver(call));
	}

	public XExpression getActualReceiver(XAbstractFeatureCall featureCall, JvmIdentifiableElement feature, XExpression implicitReceiver) {
		if (isStaticJavaFeature(feature)) {
			return null;
		}
		if (implicitReceiver!=null)
			return implicitReceiver;
		final List<? extends XExpression> allArguments = featureCall.getExplicitArguments();
		if (allArguments.isEmpty())
			return null;
		return allArguments.get(0);
	}
	
	protected JvmIdentifiableElement getFeature(XAbstractFeatureCall expr) {
		return linkingAssumptions.getFeature(expr, true);
	}
	
	protected XExpression getImplicitReceiver(XAbstractFeatureCall expr) {
		return linkingAssumptions.getImplicitReceiver(expr);
	}
	
	public List<XExpression> getActualArguments(XAbstractFeatureCall featureCall) {
		return getActualArguments(featureCall, getFeature(featureCall), getImplicitReceiver(featureCall));
	}
	
	public List<XExpression> getActualArguments(XAbstractFeatureCall featureCall, JvmIdentifiableElement feature, XExpression implicitReceiver) {
		final List<? extends XExpression> explicitArguments = featureCall.getExplicitArguments();
		if (isStaticJavaFeature(feature)) {
			if (implicitReceiver == null || explicitArguments.contains(implicitReceiver))
				return newArrayList(explicitArguments);
			List<XExpression> result = newArrayList(implicitReceiver);
			result.addAll(explicitArguments);
			return result;
		} else if (implicitReceiver != null) {
			return newArrayList(explicitArguments);
		}
		if (explicitArguments.size()<=1)
			return emptyList();
		return newArrayList(explicitArguments.subList(1, explicitArguments.size()));
	}
	
	public boolean isTargetsMemberSyntaxCall(XAbstractFeatureCall featureCall, JvmIdentifiableElement feature, XExpression implicitReceiver) {
		return !isStaticJavaFeature(feature);
	}
	
}
