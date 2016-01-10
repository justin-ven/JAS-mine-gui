package microsim.gui.shell.parameter;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import javax.swing.JInternalFrame;

import microsim.annotation.ModelParameter;

import org.metawidget.inspector.composite.CompositeInspector;
import org.metawidget.inspector.composite.CompositeInspectorConfig;
import org.metawidget.inspector.impl.BaseObjectInspector;
import org.metawidget.inspector.impl.propertystyle.Property;
import org.metawidget.swing.SwingMetawidget;
import org.metawidget.util.CollectionUtils;

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

		metawidget = new DescriptiveSwingMetawidget();
		CompositeInspectorConfig inspectorConfig = new CompositeInspectorConfig().setInspectors(							
				new ParameterInspector(),
				new TooltipInspector() );
		
	
		binder = new MetawidgetBinder();
		metawidget.addWidgetProcessor(binder);	    			   
			    	
		List<Field> fields = ParameterInspector.extractModelParameters(target.getClass());
		//metawidget.setInspector(new ParameterInspector());
		metawidget.setInspector( new CompositeInspector( inspectorConfig ) );
		metawidget.setToInspect( target );	
		setSize(250, 30 * fields.size());
				
		getContentPane().add(metawidget);
		setVisible(true);
	
	}
	
	public void save() {
		binder.save(metawidget);
	}
	
	public static class TooltipInspector
		extends BaseObjectInspector {

		protected Map<String, String> inspectProperty( Property property )
			throws Exception {
			
			Map<String, String> attributes = CollectionUtils.newHashMap();

			ModelParameter tooltip = property.getAnnotation( ModelParameter.class );

			if ( tooltip != null )
				attributes.put( "tooltip", tooltip.description() );

			return attributes;
		}
	}
}
