package pt.linkare.ant;

public abstract class TextBlock{

    private String content=null;
    
    public String getContent()
    {
	return content;
    }
    
    public abstract String toString();
    public abstract boolean equals(Object other);
    public void setContent(String content)
    {
	this.content=content;
	initializeFromContent(content);
    }

    protected abstract void initializeFromContent(String content);
    
}
