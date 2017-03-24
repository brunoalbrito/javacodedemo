package cn.java.velocity.directive;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.apache.commons.lang.text.StrBuilder;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.exception.TemplateInitException;
import org.apache.velocity.exception.VelocityException;
import org.apache.velocity.runtime.Renderable;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.directive.StopCommand;
import org.apache.velocity.runtime.log.Log;
import org.apache.velocity.runtime.parser.node.Node;

public class FooDirective  extends Directive{
	protected Log log;
    protected int maxDepth;
    protected String key;
    protected Node fooNode;
    
	@Override
	public String getName() {
		return "foo";
	}

	@Override
	public int getType() {
		return BLOCK;
	}

	public void init(RuntimeServices rs, InternalContextAdapter context, Node node)
			throws TemplateInitException
	{
		super.init(rs, context, node);

		log = rs.getLog();
		/**
         * No checking is done. We just grab the last child node and assume
         * that it's the block!
         */
		fooNode = node.jjtGetChild(node.jjtGetNumChildren() - 1);
		
		// the first child is the block name (key), the second child is the block AST body
        if ( node.jjtGetNumChildren() != 2 )
        {
            throw new VelocityException("parameter missing: block name at "
                 + Log.formatFileString(this));
        }
        
        /*
         * first token is the name of the block. We don't even check the format,
         * just assume it looks like this: $block_name. Should we check if it has
         * a '$' or not?
         */
        key = node.jjtGetChild(0).getFirstToken().image.substring(1);

        /*
         * default max depth of two is used because intentional recursion is
         * unlikely and discouraged, so make unintentional ones end fast
         */
        maxDepth = rs.getInt(RuntimeConstants.DEFINE_DIRECTIVE_MAXDEPTH, 2);
	}

	@Override
	public boolean render(InternalContextAdapter context, Writer writer, Node node)
			throws IOException, ResourceNotFoundException, ParseErrorException, MethodInvocationException {
        context.put(key, new Reference(context, this));
        return true;
	}
	
	/**
     * Creates a string identifying the source and location of the block
     * definition, and the current template being rendered if that is
     * different.
     */
    protected String id(InternalContextAdapter context)
    {
        StrBuilder str = new StrBuilder(100)
            .append("foo $").append(key);
        if (!context.getCurrentTemplateName().equals(getTemplateName()))
        {
            str.append(" used in ").append(context.getCurrentTemplateName());
        }
        return str.toString();
    }
    
    
	public boolean render(InternalContextAdapter context, Writer writer)
    {
        preRender(context);
        try
        {
            return fooNode.render(context, writer);
        }
        catch (IOException e)
        {
            String msg = "Failed to render " + id(context) + " to writer "
              + " at " + Log.formatFileString(this);

            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
        catch (StopCommand stop)
        {
            if (!stop.isFor(this))
            {
                throw stop;
            }
            return true;
        }
        finally
        {
            postRender(context);
        }
    }
	
	/**
     * actual class placed in the context, holds the context
     * being used for the render, as well as the parent (which already holds
     * everything else we need).
     */
    public static class Reference implements Renderable
    {
        private InternalContextAdapter context;
        private FooDirective parent;
        private int depth;
        
        public Reference(InternalContextAdapter context, FooDirective parent)
        {
            this.context = context;
            this.parent = parent;
        }
        
        /**
         * Render the AST of this block into the writer using the context.
         */
        public boolean render(InternalContextAdapter context, Writer writer)
        {
            depth++;
            if (depth > parent.maxDepth)
            {
                /* this is only a debug message, as recursion can
                 * happen in quasi-innocent situations and is relatively
                 * harmless due to how we handle it here.
                 * this is more to help anyone nuts enough to intentionally
                 * use recursive block definitions and having problems
                 * pulling it off properly.
                 */
                parent.log.debug("Max recursion depth reached for " + parent.id(context)
                    + " at " + Log.formatFileString(parent));
                depth--;
                return false;
            }
            else
            {
                parent.render(context, writer);
                depth--;
                return true;
            }
        }

        public String toString()
        {
            Writer writer = new StringWriter();
            if (render(context, writer))
            {
                return writer.toString();
            }
            return null;
        }
    }

}
