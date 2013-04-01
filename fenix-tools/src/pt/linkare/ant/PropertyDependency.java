package pt.linkare.ant;

public class PropertyDependency {

    private InputProperty parentProperty = null;
    private String value = "*";
    private String parentPropertyName = null;

    public PropertyDependency(String parentPropertyName, String value) {
        super();
        // TODO Auto-generated constructor stub
        this.value = value;
        this.parentPropertyName = parentPropertyName;
    }

    public PropertyDependency(InputProperty parentProperty, String value) {
        super();
        // TODO Auto-generated constructor stub
        this.parentProperty = parentProperty;
        this.value = value;
    }

    public PropertyDependency(String propSpec) {
        super();
        String propName = propSpec;
        String value = "*";
        if (propSpec.indexOf("=") != -1) {
            propName = propSpec.substring(0, propSpec.indexOf("=")).trim();
            value = propSpec.substring(propSpec.indexOf("=") + 1).trim();
        }
        this.value = value;
        this.parentPropertyName = propName;
    }

    public PropertyDependency() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @return Returns the parentProperty.
     */
    public InputProperty getParentProperty() {
        return parentProperty;
    }

    /**
     * @param parentProperty The parentProperty to set.
     */
    public void setParentProperty(InputProperty parentProperty) {
        this.parentProperty = parentProperty;
    }

    /**
     * @return Returns the value.
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value The value to set.
     */
    public void setValue(String value) {
        this.value = value;
    }

    public boolean validateDependency() {
        if (getValue().equalsIgnoreCase("*")) {
            return getParentProperty().getPropertyValue() != null;
        } else {
            if (getParentProperty() == null || getParentProperty().getPropertyValue() == null) {
                return false;
            }

            return getParentProperty().getPropertyValue().equals(getValue());
        }
    }

    /**
     * @return Returns the parentPropertyName.
     */
    public String getParentPropertyName() {
        return parentPropertyName;
    }

    /**
     * @param parentPropertyName The parentPropertyName to set.
     */
    public void setParentPropertyName(String parentPropertyName) {
        this.parentPropertyName = parentPropertyName;
    }

}
