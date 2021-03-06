/* Generated By:JJTree: Do not edit this line. ASTVarNode.java */

package com.meidusa.amoeba.sqljep;

import java.util.Map;

import com.meidusa.amoeba.sqljep.variable.Variable;


/**
 * 	It is public and fields index and variable are public 
 *  because it is possible to change values outside from SQLJEP.
 * @see com.meidusa.amoeba.sqljep.BaseJEP#getTopNode()
 */
public class ASTVarNode extends SimpleNode {
	public int index;			// index of column in a Row
	public Variable variable;
	public String ident;
	
	public ASTVarNode(int id) {
		super(id);
	}
	
	public ASTVarNode(Parser p, int id) {
		super(p, id);
	}
	
	/**
	 * Accept the visitor.
	 */
	public Object jjtAccept(ParserVisitor visitor, Object data) throws ParseException {
		return visitor.visit(this, data);
	}

	/**
	* Creates a string containing the variable's name and value
	*/
	public String toString() {
		if (variable == null) {
			return "Column: "+index;
		} else {
			return "Variable: \"" + (variable == null ? "null" : variable.getValue().toString())+ "\"";
		}
	}
}
