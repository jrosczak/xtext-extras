package org.eclipse.xtext.xbase.tests.jvmmodel;

import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import com.google.inject.MembersInjector;
import com.google.inject.Provider;
import java.util.Set;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.common.types.JvmConstructor;
import org.eclipse.xtext.common.types.JvmDeclaredType;
import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.resource.DerivedStateAwareResource;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.xbase.jvmmodel.IJvmDeclaredTypeAcceptor;
import org.eclipse.xtext.xbase.jvmmodel.IJvmModelInferrer;
import org.eclipse.xtext.xbase.jvmmodel.JvmModelAssociator;
import org.eclipse.xtext.xbase.jvmmodel.JvmTypesBuilder;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.tests.jvmmodel.AbstractJvmModelTest;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("all")
public class JvmModelAssociatorTest extends AbstractJvmModelTest {
  @Inject
  @Extension
  private JvmTypesBuilder _jvmTypesBuilder;
  
  @Inject
  private MembersInjector<JvmModelAssociator> associatorInjector;
  
  private JvmModelAssociator assoc;
  
  @Inject
  private XtextResourceSet resourceSet;
  
  @Inject
  private DerivedStateAwareResource resource;
  
  @Before
  public void createAssociator() {
    JvmModelAssociator _jvmModelAssociator = new JvmModelAssociator();
    this.assoc = _jvmModelAssociator;
    this.associatorInjector.injectMembers(this.assoc);
  }
  
  @After
  public void discardAssociator() {
    this.assoc = null;
  }
  
  @Test
  public void testInference() {
    final Provider<IJvmModelInferrer> _function = () -> {
      final IJvmModelInferrer _function_1 = (EObject obj, IJvmDeclaredTypeAcceptor acceptor, boolean preIndexing) -> {
        final JvmGenericType firstType = this._jvmTypesBuilder.toClass(obj, "foo.Bar");
        final JvmGenericType secondType = this._jvmTypesBuilder.toClass(obj, "foo.Baz");
        Assert.assertNull(secondType.eResource());
        final Procedure1<JvmGenericType> _function_2 = (JvmGenericType it) -> {
          it.setAbstract(true);
          Assert.assertNotNull(firstType.eResource());
          Assert.assertNotNull(secondType.eResource());
        };
        acceptor.<JvmGenericType>accept(firstType, _function_2);
        final Procedure1<JvmGenericType> _function_3 = (JvmGenericType it) -> {
          it.setAbstract(true);
          Assert.assertNotNull(firstType.eResource());
        };
        acceptor.<JvmGenericType>accept(secondType, _function_3);
      };
      return _function_1;
    };
    this.assoc.setInferrerProvider(_function);
    this.resource.setDerivedStateComputer(null);
    this.resource.setURI(URI.createURI("foo.txt"));
    this.resourceSet.setClasspathURIContext(this.getClass());
    EList<Resource> _resources = this.resourceSet.getResources();
    this._jvmTypesBuilder.<DerivedStateAwareResource>operator_add(_resources, this.resource);
    EList<EObject> _contents = this.resource.getContents();
    EClass _createEClass = EcoreFactory.eINSTANCE.createEClass();
    this._jvmTypesBuilder.<EClass>operator_add(_contents, _createEClass);
    this.assoc.installDerivedState(this.resource, true);
    EObject _get = this.resource.getContents().get(1);
    Assert.assertFalse(((JvmDeclaredType) _get).isAbstract());
    this.resource.getContents().clear();
    EList<EObject> _contents_1 = this.resource.getContents();
    EClass _createEClass_1 = EcoreFactory.eINSTANCE.createEClass();
    this._jvmTypesBuilder.<EClass>operator_add(_contents_1, _createEClass_1);
    this.assoc.installDerivedState(this.resource, false);
    EObject _get_1 = this.resource.getContents().get(1);
    final JvmGenericType type = ((JvmGenericType) _get_1);
    Assert.assertTrue(type.isAbstract());
    Assert.assertEquals(1, IterableExtensions.size(Iterables.<JvmConstructor>filter(type.getMembers(), JvmConstructor.class)));
    JvmTypeReference _head = IterableExtensions.<JvmTypeReference>head(type.getSuperTypes());
    String _qualifiedName = null;
    if (_head!=null) {
      _qualifiedName=_head.getQualifiedName();
    }
    Assert.assertEquals("java.lang.Object", _qualifiedName);
  }
  
  @Test
  public void testRemoveAssociation() {
    final Provider<IJvmModelInferrer> _function = () -> {
      final IJvmModelInferrer _function_1 = (EObject obj, IJvmDeclaredTypeAcceptor acceptor, boolean preIndexing) -> {
        final JvmGenericType firstType = this._jvmTypesBuilder.toClass(obj, "foo.Bar");
        final JvmGenericType secondType = this._jvmTypesBuilder.toClass(obj, "foo.Baz");
        Assert.assertNull(secondType.eResource());
        final Procedure1<JvmGenericType> _function_2 = (JvmGenericType it) -> {
          it.setAbstract(true);
          Assert.assertNotNull(firstType.eResource());
          Assert.assertNotNull(secondType.eResource());
        };
        acceptor.<JvmGenericType>accept(firstType, _function_2);
        final Procedure1<JvmGenericType> _function_3 = (JvmGenericType it) -> {
          it.setAbstract(true);
          Assert.assertNotNull(firstType.eResource());
        };
        acceptor.<JvmGenericType>accept(secondType, _function_3);
      };
      return _function_1;
    };
    this.assoc.setInferrerProvider(_function);
    this.resource.setDerivedStateComputer(null);
    this.resource.setURI(URI.createURI("foo.txt"));
    this.resourceSet.setClasspathURIContext(this.getClass());
    EList<Resource> _resources = this.resourceSet.getResources();
    this._jvmTypesBuilder.<DerivedStateAwareResource>operator_add(_resources, this.resource);
    final EClass root = EcoreFactory.eINSTANCE.createEClass();
    EList<EObject> _contents = this.resource.getContents();
    this._jvmTypesBuilder.<EClass>operator_add(_contents, root);
    this.assoc.installDerivedState(this.resource, true);
    Set<EObject> jvmElements = this.assoc.getJvmElements(root);
    Assert.assertEquals(2, jvmElements.size());
    final Set<EObject> _converted_jvmElements = (Set<EObject>)jvmElements;
    final EObject jvmElement1 = ((EObject[])Conversions.unwrapArray(_converted_jvmElements, EObject.class))[0];
    final Set<EObject> _converted_jvmElements_1 = (Set<EObject>)jvmElements;
    final EObject jvmElement2 = ((EObject[])Conversions.unwrapArray(_converted_jvmElements_1, EObject.class))[1];
    Set<EObject> sources1 = this.assoc.getSourceElements(jvmElement1);
    Assert.assertEquals(1, sources1.size());
    final Set<EObject> _converted_sources1 = (Set<EObject>)sources1;
    Assert.assertEquals(root, ((Object[])Conversions.unwrapArray(_converted_sources1, Object.class))[0]);
    Set<EObject> sources2 = this.assoc.getSourceElements(jvmElement2);
    Assert.assertEquals(1, sources2.size());
    final Set<EObject> _converted_sources2 = (Set<EObject>)sources2;
    Assert.assertEquals(root, ((Object[])Conversions.unwrapArray(_converted_sources2, Object.class))[0]);
    this.assoc.removeAssociation(root, jvmElement2);
    jvmElements = this.assoc.getJvmElements(root);
    Assert.assertEquals(1, jvmElements.size());
    sources1 = this.assoc.getSourceElements(jvmElement1);
    Assert.assertEquals(1, sources1.size());
    final Set<EObject> _converted_sources1_1 = (Set<EObject>)sources1;
    Assert.assertEquals(root, ((Object[])Conversions.unwrapArray(_converted_sources1_1, Object.class))[0]);
    sources2 = this.assoc.getSourceElements(jvmElement2);
    Assert.assertEquals(0, sources2.size());
  }
}
