import java.util.*;

public class TokenGenerator {
	private Random rand;
	private TreeSet<String> idUniverse;
	
	private BasicType currentType;
	
	public TokenGenerator() {
		rand = new Random();
		idUniverse = new TreeSet<String>();
		// Place all lowercase and uppercase letters as unused identifiers
		for(int i = 0; i < 26; i++) {
			Character idLower = (char) ('a' + i);
			Character idUpper = (char) ('A' + i);
			idUniverse.add(idLower.toString());
			idUniverse.add(idUpper.toString());
		}
		currentType = BasicType.UNKNOWN;
	}
	
	private boolean isIntType(BasicType type) {
		if(type == BasicType.BYTE || type == BasicType.INT
				|| type == BasicType.SHORT || type == BasicType.LONG || type == BasicType.CHAR)
			return true;
		else
			return false;
	}
	
	private boolean isFloatType(BasicType type) {
		if(type == BasicType.DOUBLE || type == BasicType.FLOAT)
			return true;
		else
			return false;
	}
	
	private String generateAssignmentOperator() {
		String ret = "";
		return ret;
	}
	
	private String generatePrimary() {
		String ret = "";
		boolean literal = true;//rand.nextBoolean();
		if(literal) {
			switch(currentType) {
			case BOOLEAN:
				ret = rand.nextBoolean() + "";
				break;
			case INT:
				ret = rand.nextInt(100) + "";
				break;
			case LONG:
				ret = rand.nextLong() + "";
				break;
			case BYTE:
				ret = String.format("0x%x", (byte)(rand.nextInt() >> 24));
				break;
			case SHORT:
				ret = (rand.nextInt() >> 16) + "";
				break;
			case FLOAT:
				ret = rand.nextFloat() + rand.nextInt(101) + "";
				break;
			case DOUBLE:
				ret = rand.nextDouble() + rand.nextInt(101) + "";
				break;
			case CHAR:
				char tmp = (char)(rand.nextInt(94) + 32);
				ret = "'" + tmp + "'";
				break;
			}
		}
		else
			ret = generateId();
		return ret;
	}
	
	private String generateSelector() {
		String ret = "";
		return ret;
	}
	
	private String generatePostfixOp() {
		String ret = "";
		return ret;
	}
	
	private String generatePrefixOp() {
		String ret = "";
		return ret;
	}
	
	private String generateArithmeticOp() {
		String ret = "";
		int op;
		boolean isInt = currentType == BasicType.INT || currentType == BasicType.SHORT || currentType == BasicType.LONG;
		// If it isn't an int, don't let modulo happen
		if(isInt)
			op = rand.nextInt(5);
		else
			op = rand.nextInt(4);
		
		switch(op) {
		case 0:
			ret = "+";
			break;
		case 1:
			ret = "-";
			break;
		case 2:
			ret = "/";
			break;
		case 3:
			ret = "*";
			break;
		case 4:
			ret = "%";
			break;
		}
		return ret;
	}
	
	private String generateBinaryOp() {
		String ret = "";
		int op = rand.nextInt(6);
		switch(op) {
		case 0:
			ret = "<<";
			break;
		case 1:
			ret = ">>";
			break;
		case 2:
			ret = ">>>";
			break;
		case 3:
			ret = "|";
			break;
		case 4:
			ret = "&";
			break;
		case 5:
			ret = "^";
			break;
		}
		return ret;
	}
	
	private String generateRelationalOp() {
		String ret = "";
		int op = rand.nextInt(11);
		switch(op) {
		case 0:
			ret = "||";
			break;
		case 1:
			ret = "&&";
			break;
		case 5:
			ret = "==";
			break;
		case 6:
			ret = "!=";
			break;
		case 7:
			ret = "<";
			break;
		case 8:
			ret = ">";
			break;
		case 9:
			ret = "<=";
			break;
		case 10:
			ret = ">=";
			break;
		}
		return ret;
	}
	
	private String generateInfixOp() {
		String ret = "";
		
		if(isIntType(currentType)) {
			int type = rand.nextInt(2);
			switch(type) {
			case 0:
				ret = generateArithmeticOp();
				break;
			case 1:
				ret = generateBinaryOp();
				break;
			}
		}
		else if(isFloatType(currentType))
			ret = generateArithmeticOp();
		else
			ret = generateRelationalOp();

		return ret;
	}
	
	private String generateExpression3() {
		String ret = "";
		int choices = 2;//rand.nextInt(3);
		switch(choices) {
			// prefix_op expression3
			case 0:
				//ret = generatePrefixOp() + generateExpression3();
				break;
			// ((expression | type)) expression3
			case 1:
				//boolean option = rand.nextBoolean();
				//if(option)
				//	ret = generateExpression();
				//else
				//	ret = generateType();
				//ret += generateExpression3();
				break;
			// primary {selector} {postfix_op}
			case 2:
				ret = generatePrimary();
				//boolean has_sel = rand.nextBoolean();
				//boolean has_postfix = rand.nextBoolean();
				
				//if(has_sel)
				//	ret += generateSelector();
				//if(has_postfix)
				//	ret += generatePostfixOp();
				break;
		}
		return ret;
	}
	
	private String generateExpression2Rest() {
		String ret = "";
		boolean optional = rand.nextBoolean();
		if(optional)
			ret = " " + generateInfixOp() + " " + generateExpression3();
		return ret;
	}
	
	private String generateExpression2() {
		String ret = generateExpression3();
		boolean optional = rand.nextBoolean();
		if(optional)
			ret += generateExpression2Rest();
		return ret;
	}
	
	private String generateExpression1Rest() {
		String ret = "?" + generateExpression() + generateExpression1();
		return ret;
	}
	
	private String generateExpression1() {
		String ret = generateExpression2();
		//boolean optional = rand.nextBoolean();
		//if(optional)
		//	ret += generateExpression1Rest();
		return ret;
	}
	
	public String generateExpression() {
		String ret = generateExpression1();
		//boolean optional = rand.nextBoolean();
		//if(optional)
		//	ret += generateAssignmentOperator() + generateExpression1();
		return ret;
	}
	
	// No arrays yet
	public String generateVariableInitializer() {
		String ret = "";
		ret = generateExpression();
		return ret;
	}
	
	public String generateVariableDeclaratorRest() {
		String ret = "";
		boolean initialized = true; //rand.nextBoolean();
		if(initialized)
			ret = " = " + generateVariableInitializer();
		return ret;
	}
	
	public String generateVariableDeclarator() {
		String ret = generateId() + generateVariableDeclaratorRest();
		return ret;
	}
	
	public String generateVariableDeclarators() {
		String ret = generateVariableDeclarator();
		return ret;
	}
	
	public String generateLocalVariableDeclaration() {
		String ret = "";
		ret = generateType() + " " + generateVariableDeclarators() + ";";
		return ret;
	}
	
	// ID -> [a-zA-Z].[a-zA-Z0-9]*
	// I don't want random ID's to begin with, becuase that won't be readable.
	// To start, I'll simply pick alphabetic letters.
	// I'm ignoring qualified identifiers for now
	private String generateId() {
		String ret = idUniverse.pollFirst();	// Takes the first element out of the tree
		return ret;
	}
	
	// type -> basic_type | reference_type
	// For the time being, I'm ignoring the possibility of a reference type
	private String generateType() {
		boolean array = false; // when we implement arrays, set this to a random bool
		String ret = "";
		if(array) 
			ret = generateBasicType() + "[]";
		else
			ret = generateBasicType();
		return ret;
	}
	
	// Randomly generates a basic data-type
	private String generateBasicType() {
		int type = rand.nextInt(8); // Generate a number between 0 and 7
		String ret = "Undefined";
		switch(type) {
			case 0:
				ret = "byte";
				currentType = BasicType.BYTE;
				break;
			case 1:
				ret = "short";
				currentType = BasicType.SHORT;
				break;
			case 2:
				ret = "char";
				currentType = BasicType.CHAR;
				break;
			case 3:
				ret = "int";
				currentType = BasicType.INT;
				break;
			case 4:
				ret = "long";
				currentType = BasicType.LONG;
				break;
			case 5:
				ret = "float";
				currentType = BasicType.FLOAT;
				break;
			case 6:
				ret = "double";
				currentType = BasicType.DOUBLE;
				break;
			case 7:
				ret = "boolean";
				currentType = BasicType.BOOLEAN;
				break;	
		}
		return ret;
	}
	
	public static void main(String[] args) {
		TokenGenerator generator = new TokenGenerator();
		for(int i = 0; i < 10; i++)
			System.out.println(generator.generateLocalVariableDeclaration());
	}
}
