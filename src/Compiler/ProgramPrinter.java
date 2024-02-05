package Compiler;

import gen.MiniJavaListener;
import gen.MiniJavaParser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

public class ProgramPrinter implements MiniJavaListener {

    public static int tabCount = 0;
    public static String tab = "    ";
    public static int nested = 0;

    public boolean isNotInterface = true;
    public static int parametersCount = 0;
    public static void repeatTab(int count){
        System.out.print(new String(new char[count]).replace("\0", tab));
    }

    public static void repeatStr(String str) {
        for (int i = 0; i < tabCount; i++) {
            System.out.print(tab);
        }
        System.out.println(str);
    }

    public static void printBrackets(char bracket){
        System.out.print(bracket);
    }

    public static void printWord(String keyWord) {
        System.out.print(keyWord + " ");
    }

    public static void printLastWord(String keyWord){
        System.out.println(keyWord);
    }

    @Override
    public void enterProgram(MiniJavaParser.ProgramContext ctx) {

    }

    @Override
    public void exitProgram(MiniJavaParser.ProgramContext ctx) {

    }

    @Override
    public void enterMainClass(MiniJavaParser.MainClassContext ctx) {
        repeatTab(tabCount);
        tabCount++;
        System.out.println("class " + ctx.className.getText() + "{");
    }

    @Override
    public void exitMainClass(MiniJavaParser.MainClassContext ctx) {
        tabCount--;
        repeatTab(tabCount);
        System.out.println("}");
    }

    @Override
    public void enterMainMethod(MiniJavaParser.MainMethodContext ctx) {
        repeatTab(tabCount);
        for (int i = 0; i < 4; i++)
            printWord(ctx.getChild(i).getText());
        printBrackets('(');
        printWord("String[] args");
        printBrackets(')');
        printLastWord("{");
        tabCount++;
    }

    @Override
    public void exitMainMethod(MiniJavaParser.MainMethodContext ctx) {
        tabCount--;
        repeatStr("}");
    }

    @Override
    public void enterClassDeclaration(MiniJavaParser.ClassDeclarationContext ctx) {
        isNotInterface = true;
        repeatTab(tabCount);
        tabCount++;
        printWord("class " + ctx.className.getText());

        boolean hasInheritance = ctx.getText().contains("inherits");
        if (hasInheritance)
            printWord("extends " + ctx.Identifier(1));

        boolean hasImplementation = ctx.getText().contains("implements");
        if (hasImplementation) {
            StringBuilder interfaceList = new StringBuilder(ctx.Identifier(1).getText());
            for (int i = 3; i < ctx.Identifier().size(); i++)
                interfaceList.append(",").append(ctx.Identifier(i).getText());
            System.out.print("implements " + interfaceList);
        }
        System.out.println(" {\n");
    }

    @Override
    public void exitClassDeclaration(MiniJavaParser.ClassDeclarationContext ctx) {
        isNotInterface = false;
        tabCount--;
        repeatTab(tabCount);
        System.out.println("}");
    }

    @Override
    public void enterInterfaceDeclaration(MiniJavaParser.InterfaceDeclarationContext ctx) {
        System.out.println("interface " + ctx.Identifier().getText() + " {");
        isNotInterface = false;
        tabCount++;
    }

    @Override
    public void exitInterfaceDeclaration(MiniJavaParser.InterfaceDeclarationContext ctx) {
        isNotInterface = true;
        tabCount--;
        repeatTab(tabCount);
        System.out.println("}");
    }

    @Override
    public void enterInterfaceMethodDeclaration(MiniJavaParser.InterfaceMethodDeclarationContext ctx) {
        repeatTab(tabCount);
        if (ctx.accessModifier() != null)
            printWord(ctx.accessModifier().getText());
        String fieldType = ctx.returnType().getText();
        if (fieldType.equals("number"))
            printWord("int");
        else
            printWord(fieldType);
        printWord(ctx.Identifier().getText() + "(");
    }

    @Override
    public void exitInterfaceMethodDeclaration(MiniJavaParser.InterfaceMethodDeclarationContext ctx) {
        printLastWord(");");
    }

    @Override
    public void enterFieldDeclaration(MiniJavaParser.FieldDeclarationContext ctx) {
        String fieldType = ctx.type().getText();
        repeatTab(tabCount);
        if (ctx.accessModifier() != null)
            printWord(ctx.accessModifier().getText());
        if (ctx.Final() != null)
            printWord(ctx.Final().getText());
        if (fieldType.equals("number"))
            printWord("int");
        else
            printWord(fieldType);
        printWord(ctx.Identifier().getText());
        if (ctx.EQ() != null)
            printWord("=");
    }

    @Override
    public void exitFieldDeclaration(MiniJavaParser.FieldDeclarationContext ctx) {
        printLastWord(";");
    }

    @Override
    public void enterLocalDeclaration(MiniJavaParser.LocalDeclarationContext ctx) {
        repeatTab(tabCount);
        String fieldType = ctx.type().getText();
        if (fieldType.equals("number"))
            printWord("int");
        else
            printWord(fieldType);
        printLastWord(ctx.Identifier().getText() + ";");
    }

    @Override
    public void exitLocalDeclaration(MiniJavaParser.LocalDeclarationContext ctx) {
        // No exit
    }

    @Override
    public void enterMethodDeclaration(MiniJavaParser.MethodDeclarationContext ctx) {
        if (ctx.Override() != null) {
            repeatTab(tabCount);
            printLastWord(ctx.Override().getText());
        }
        repeatTab(tabCount);
        if (ctx.accessModifier() != null)
            printWord(ctx.accessModifier().getText());
        String fieldType = ctx.returnType().getText();
        if (fieldType.equals("number"))
            printWord("int");
        else
            printWord(fieldType);
        printWord(ctx.Identifier().getText() + "(");
        if (ctx.parameterList() == null)
            printLastWord(") {");
        tabCount++;
    }

    @Override
    public void exitMethodDeclaration(MiniJavaParser.MethodDeclarationContext ctx) {
        if (ctx.getText().contains("ret")) {
            repeatTab(tabCount);
            printWord("return");
        }
//        printLastWord(ctx.getChild(ctx.getChildCount() - 2).getText() + ";");
        tabCount--;
        repeatStr("}");
    }

    @Override
    public void enterParameterList(MiniJavaParser.ParameterListContext ctx) {
        parametersCount = ctx.parameter().size();
    }

    @Override
    public void exitParameterList(MiniJavaParser.ParameterListContext ctx) {
        parametersCount = 0;
        if (isNotInterface)
            printLastWord(") {");
    }

    @Override
    public void enterParameter(MiniJavaParser.ParameterContext ctx) {
        printWord(ctx.type().getText());
        if (parametersCount > 1)
            printWord(ctx.Identifier().getText() + ",");
         else
            printWord(ctx.Identifier().getText());
        parametersCount--;
    }

    @Override
    public void exitParameter(MiniJavaParser.ParameterContext ctx) {
        // No exit
    }

    @Override
    public void enterMethodBody(MiniJavaParser.MethodBodyContext ctx) {
    }

    @Override
    public void exitMethodBody(MiniJavaParser.MethodBodyContext ctx) {
        if (ctx.getText().contains("return")) {
            repeatTab(tabCount);
            printLastWord("return " + ctx.expression().getText() + ";");
            tabCount--;
            printLastWord("}");
        }
    }

    @Override
    public void enterType(MiniJavaParser.TypeContext ctx) {

    }

    @Override
    public void exitType(MiniJavaParser.TypeContext ctx) {
        // No exit
    }

    @Override
    public void enterBooleanType(MiniJavaParser.BooleanTypeContext ctx) {
//        System.out.print("boolean");
    }

    @Override
    public void exitBooleanType(MiniJavaParser.BooleanTypeContext ctx) {
        // No exit
    }

    @Override
    public void enterReturnType(MiniJavaParser.ReturnTypeContext ctx) {
//        if (ctx.getText().contains("void"))
//            System.out.print("void");
//        else if (ctx.type() != null)
//            System.out.print(ctx.type().getText());
    }

    @Override
    public void exitReturnType(MiniJavaParser.ReturnTypeContext ctx) {
        // No exit
    }

    @Override
    public void enterAccessModifier(MiniJavaParser.AccessModifierContext ctx) {
    }

    @Override
    public void exitAccessModifier(MiniJavaParser.AccessModifierContext ctx) {
        // No exit
    }

    @Override
    public void enterNestedStatement(MiniJavaParser.NestedStatementContext ctx) {
        if (nested > 0) {
            repeatStr("{");
            tabCount++;
        }
        nested++;
    }

    @Override
    public void exitNestedStatement(MiniJavaParser.NestedStatementContext ctx) {
        nested--;
        if (nested > 0) {
            tabCount--;
            repeatStr("}");
        }
    }

    @Override
    public void enterIfElseStatement(MiniJavaParser.IfElseStatementContext ctx) {
        repeatTab(tabCount);
        printWord("if");
        printBrackets('(');
    }

    @Override
    public void exitIfElseStatement(MiniJavaParser.IfElseStatementContext ctx) {

    }

    @Override
    public void enterWhileStatement(MiniJavaParser.WhileStatementContext ctx) {
        repeatTab(tabCount);
        printWord("while (");
        tabCount++;
    }

    @Override
    public void exitWhileStatement(MiniJavaParser.WhileStatementContext ctx) {
        tabCount--;
        repeatStr("}");
    }

    @Override
    public void enterPrintStatement(MiniJavaParser.PrintStatementContext ctx) {
        repeatTab(tabCount);
        printWord("System.out.println(");
    }

    @Override
    public void exitPrintStatement(MiniJavaParser.PrintStatementContext ctx) {
        printLastWord(");");
    }

    @Override
    public void enterVariableAssignmentStatement(MiniJavaParser.VariableAssignmentStatementContext ctx) {
        repeatTab(tabCount);
        printWord(ctx.expression(0).getText() + " =");
    }

    @Override
    public void exitVariableAssignmentStatement(MiniJavaParser.VariableAssignmentStatementContext ctx) {
        printLastWord(";");
    }

    @Override
    public void enterArrayAssignmentStatement(MiniJavaParser.ArrayAssignmentStatementContext ctx) {
        repeatTab(tabCount);
        System.out.println(ctx.Identifier().getText() + "[" + ctx.expression(0).getText() + "] = " + ctx.expression(1).getText() + ";");
    }

    @Override
    public void exitArrayAssignmentStatement(MiniJavaParser.ArrayAssignmentStatementContext ctx) {
        // No exit
    }

    @Override
    public void enterLocalVarDeclaration(MiniJavaParser.LocalVarDeclarationContext ctx) {
    }

    @Override
    public void exitLocalVarDeclaration(MiniJavaParser.LocalVarDeclarationContext ctx) {
        // No exit
    }

    @Override
    public void enterExpressioncall(MiniJavaParser.ExpressioncallContext ctx) {

    }

    @Override
    public void exitExpressioncall(MiniJavaParser.ExpressioncallContext ctx) {

    }

    @Override
    public void enterIfBlock(MiniJavaParser.IfBlockContext ctx) {
        printLastWord("{");
        tabCount++;
    }

    @Override
    public void exitIfBlock(MiniJavaParser.IfBlockContext ctx) {
        tabCount--;
        repeatStr("}");
    }

    @Override
    public void enterElseBlock(MiniJavaParser.ElseBlockContext ctx) {
        repeatTab(tabCount);
        printWord("else");
        printLastWord("{");
        tabCount++;
//        System.out.println(ctx.statement().getText());
    }

    @Override
    public void exitElseBlock(MiniJavaParser.ElseBlockContext ctx) {
        tabCount--;
        printLastWord("}");
    }

    @Override
    public void enterWhileBlock(MiniJavaParser.WhileBlockContext ctx) {
        printLastWord(") {");
    }

    @Override
    public void exitWhileBlock(MiniJavaParser.WhileBlockContext ctx) {

    }

    @Override
    public void enterLtExpression(MiniJavaParser.LtExpressionContext ctx) {
//        System.out.print(ctx.expression(0).getText() + " < " + ctx.expression(1).getText());
    }

    @Override
    public void exitLtExpression(MiniJavaParser.LtExpressionContext ctx) {

    }

    @Override
    public void enterObjectInstantiationExpression(MiniJavaParser.ObjectInstantiationExpressionContext ctx) {
        System.out.print("new " + ctx.Identifier().getText() + "()");
    }

    @Override
    public void exitObjectInstantiationExpression(MiniJavaParser.ObjectInstantiationExpressionContext ctx) {
        // No exit
    }

    @Override
    public void enterArrayInstantiationExpression(MiniJavaParser.ArrayInstantiationExpressionContext ctx) {
        String type;
        if (ctx.Identifier() != null)
            type = ctx.Identifier().getText();
        else if (ctx.getText().contains("number"))
            type = "int";
        else
            type = "boolean";
        printWord("new " + type + "[");
    }

    @Override
    public void exitArrayInstantiationExpression(MiniJavaParser.ArrayInstantiationExpressionContext ctx) {
        printWord("]");
    }

    @Override
    public void enterPowExpression(MiniJavaParser.PowExpressionContext ctx) {

    }

    @Override
    public void exitPowExpression(MiniJavaParser.PowExpressionContext ctx) {

    }

    @Override
    public void enterIdentifierExpression(MiniJavaParser.IdentifierExpressionContext ctx) {
//        System.out.print(ctx.Identifier().getText());
    }

    @Override
    public void exitIdentifierExpression(MiniJavaParser.IdentifierExpressionContext ctx) {
        // No exit
    }

    @Override
    public void enterMethodCallExpression(MiniJavaParser.MethodCallExpressionContext ctx) {

    }

    @Override
    public void exitMethodCallExpression(MiniJavaParser.MethodCallExpressionContext ctx) {

    }

    @Override
    public void enterNotExpression(MiniJavaParser.NotExpressionContext ctx) {
        printBrackets('!');
    }

    @Override
    public void exitNotExpression(MiniJavaParser.NotExpressionContext ctx) {
        // No exit
    }

    @Override
    public void enterBooleanLitExpression(MiniJavaParser.BooleanLitExpressionContext ctx) {
        System.out.print(ctx.BooleanLiteral().getText());
    }

    @Override
    public void exitBooleanLitExpression(MiniJavaParser.BooleanLitExpressionContext ctx) {
        // No exit
    }

    @Override
    public void enterParenExpression(MiniJavaParser.ParenExpressionContext ctx) {
        printWord("(");
    }

    @Override
    public void exitParenExpression(MiniJavaParser.ParenExpressionContext ctx) {
        printWord(")");
    }

    @Override
    public void enterIntLitExpression(MiniJavaParser.IntLitExpressionContext ctx) {
        printWord(ctx.IntegerLiteral().getText());
    }

    @Override
    public void exitIntLitExpression(MiniJavaParser.IntLitExpressionContext ctx) {

    }

    @Override
    public void enterStringLitExpression(MiniJavaParser.StringLitExpressionContext ctx) {

    }

    @Override
    public void exitStringLitExpression(MiniJavaParser.StringLitExpressionContext ctx) {

    }

    @Override
    public void enterNullLitExpression(MiniJavaParser.NullLitExpressionContext ctx) {
        printWord("null");
    }

    @Override
    public void exitNullLitExpression(MiniJavaParser.NullLitExpressionContext ctx) {

    }

    @Override
    public void enterAndExpression(MiniJavaParser.AndExpressionContext ctx) {
        System.out.print(ctx.expression(0).getText() + " && " + ctx.expression(1).getText());
    }

    @Override
    public void exitAndExpression(MiniJavaParser.AndExpressionContext ctx) {
        //No exit
    }

    @Override
    public void enterArrayAccessExpression(MiniJavaParser.ArrayAccessExpressionContext ctx) {
        printWord(ctx.expression(1).getText());
        printBrackets('[');
    }

    @Override
    public void exitArrayAccessExpression(MiniJavaParser.ArrayAccessExpressionContext ctx) {
        printWord("]");
    }

    @Override
    public void enterAddExpression(MiniJavaParser.AddExpressionContext ctx) {

    }

    @Override
    public void exitAddExpression(MiniJavaParser.AddExpressionContext ctx) {

    }

    @Override
    public void enterThisExpression(MiniJavaParser.ThisExpressionContext ctx) {

    }

    @Override
    public void exitThisExpression(MiniJavaParser.ThisExpressionContext ctx) {

    }

    @Override
    public void enterFieldCallExpression(MiniJavaParser.FieldCallExpressionContext ctx) {

    }

    @Override
    public void exitFieldCallExpression(MiniJavaParser.FieldCallExpressionContext ctx) {

    }

    @Override
    public void enterArrayLengthExpression(MiniJavaParser.ArrayLengthExpressionContext ctx) {
//        repeatTab(tabCount);
    }

    @Override
    public void exitArrayLengthExpression(MiniJavaParser.ArrayLengthExpressionContext ctx) {
        printWord(".length");
    }

    @Override
    public void enterIntarrayInstantiationExpression(MiniJavaParser.IntarrayInstantiationExpressionContext ctx) {

    }

    @Override
    public void exitIntarrayInstantiationExpression(MiniJavaParser.IntarrayInstantiationExpressionContext ctx) {

    }

    @Override
    public void enterSubExpression(MiniJavaParser.SubExpressionContext ctx) {

    }

    @Override
    public void exitSubExpression(MiniJavaParser.SubExpressionContext ctx) {

    }

    @Override
    public void enterMulExpression(MiniJavaParser.MulExpressionContext ctx) {

    }

    @Override
    public void exitMulExpression(MiniJavaParser.MulExpressionContext ctx) {

    }

    @Override
    public void visitTerminal(TerminalNode terminalNode) {

    }

    @Override
    public void visitErrorNode(ErrorNode errorNode) {

    }

    @Override
    public void enterEveryRule(ParserRuleContext parserRuleContext) {

    }

    @Override
    public void exitEveryRule(ParserRuleContext parserRuleContext) {

    }
}
