
package org.limy.directwebremoting.webwork;

import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.annotations.RemoteProperty;

/**
 * A simple WebWork action invocation definition
 * (it is mapped to a <code>com.opensymphony.webwork.dispatcher.mapper.ActionMapping</code>).
 *
 * @author <a href='mailto:the_mindstorm[at]evolva[dot]ro'>Alexandru Popescu</a>
 */
@DataTransferObject
public class ActionDefinition
{
    /**
     * @return Returns the action.
     */
    @RemoteProperty
    public String getAction()
    {
        return action;
    }

    /**
     * @param action The action to set.
     */
    public void setAction(String action)
    {
        this.action = action;
    }

    /**
     * @return Returns the executeResult.
     */
    @RemoteProperty
    public boolean isExecuteResult()
    {
        return executeResult;
    }

    /**
     * @param executeResult The executeResult to set.
     */
    public void setExecuteResult(boolean executeResult)
    {
        this.executeResult = executeResult;
    }

    /**
     * @return Returns the method.
     */
    @RemoteProperty
    public String getMethod()
    {
        return method;
    }

    /**
     * @param method The method to set.
     */
    public void setMethod(String method)
    {
        this.method = method;
    }

    /**
     * @return Returns the namespace.
     */
    @RemoteProperty
    public String getNamespace()
    {
        return namespace;
    }

    /**
     * @param namespace The namespace to set.
     */
    public void setNamespace(String namespace)
    {
        this.namespace = namespace;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return "[ActionDefinition: namespace=" + namespace + " action=" + action + " method=" + method + " executeResult=" + executeResult + "]";
    }

    @RemoteProperty
    protected String namespace;

    @RemoteProperty
    protected String action;

    @RemoteProperty
    protected String method;

    @RemoteProperty
    protected boolean executeResult;
}
