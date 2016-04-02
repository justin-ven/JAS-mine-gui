package microsim.gui.shell.parameter;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import javax.swing.JInternalFrame;

import microsim.annotation.GUIparameter;
import microsim.annotation.ModelParameter;

import org.metawidget.inspector.composite.CompositeInspector;
import org.metawidget.inspector.composite.CompositeInspectorConfig;
import org.metawidget.inspector.impl.BaseObjectInspector;
import org.metawidget.inspector.impl.propertystyle.Property;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.util.CollectionUtils;

@SuppressWarnings("deprecation")
public class ParameterFrame extends JInternalFrame {
	
	private static final long serialVersionUID = 1L;

	private Object target;
	
	private MetawidgetBinder binder;
	
	private SwingMetawidget metawidget;
	
	public ParameterFrame(Object target) {
		super();
		
		this.target = target;
		
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception {
		this.setResizable(true);
		this.setTitle(target.getClass().getSimpleName() + "'s parameters");
		
		List<Field> fields = ParameterInspector.extractModelParameters(target.getClass());
		
		metawidget = new DescriptiveSwingMetawidget();
		CompositeInspectorConfig inspectorConfig = new CompositeInspectorConfig().setInspectors(							
				new ParameterInspector(),
				new TooltipInspector(fields) );
		
		binder = new MetawidgetBinder();
		metawidget.addWidgetProcessor(binder);	    			   
			    	
		//metawidget.setInspector(new ParameterInspector());
		metawidget.setInspector( new CompositeInspector( inspectorConfig ) );
		metawidget.setToInspect( target );	
		setSize(250, Math.max(30 * fields.size(), 50));
				
		getContentPane().add(metawidget);
		setVisible(true);
	
	}
	
	public void save() {
		binder.save(metawidget);
	}
	
	public static class TooltipInspector
		extends BaseObjectInspector {
		
		Map<String, String> guiParamDescriptions;
		
		TooltipInspector(List<Field> fields) {
			
			guiParamDescriptions = CollectionUtils.newHashMap();
			for(Field f : fields) {
				String description = null;
				try {
					description = f.getAnnotation(GUIparameter.class).description();
					if(description == null)
						description = f.getAnnotation(ModelParameter.class).description();	//Old deprecated version
					if(description != null) {
						guiParamDescriptions.put(f.getName(), description);	
					}	
				}
				catch (NullPointerException e) {
					//Do nothing
				}
			}
		}

//		@Override
		protected Map<String, String> inspectProperty( Property property )
			throws Exception {
			
			Map<String, String> attributes = CollectionUtils.newHashMap();
			
//			ModelParameter tooltip = property.getAnnotation( ModelParameter.class );	//Always returns null for some reason - property doesn't have the annotation information
			
			String description = guiParamDescriptions.get(property.getName());
//			if ( tooltip != null ) {		//Always null as property doesn't contain annotation
			if( description != null ) {
				attributes.put( "tooltip", description );
			}

			return attributes;
		}
	}
}
