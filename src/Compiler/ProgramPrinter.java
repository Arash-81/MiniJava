package Compiler;

import gen.MiniJavaListener;
import gen.MiniJavaParser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.Locale;

public class ProgramPrinter implements MiniJavaListener {

    public static int tabCount = 1;
    public static String tab = "    ";

    public static void repeatTab(int count) {
        System.out.println(new String(new char[count]).replace("\0", tab));
    }

    public static void repeatStr(int count, String str) {
        System.out.println(new String(new char[count]).replace("\0", str));
    }

    public static void printWord(String keyWord) {
        System.out.print(keyWord + " ");
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

    }

    @Override
    public void exitMainMethod(MiniJavaParser.MainMethodContext ctx) {

    }

    @Override
    public void enterClassDeclaration(MiniJavaParser.ClassDeclarationContext ctx) {
        repeatTab(tabCount);
        tabCount++;
        printWord("class " + ctx.className.getText().toLowerCase(Locale.ROOT));

        boolean hasInheritance = ctx.getText().contains("extends");
        if (hasInheritance)
            printWord("extends " + ctx.Identifier(1));

        boolean hasImplementation = ctx.getText().contains("implements");
        if (hasImplementation) {
            StringBuilder interfaceList = new StringBuilder(ctx.Identifier(2).getText());
            for (int i = 3; i < ctx.Identifier().size(); i++)
                interfaceList.append(",").append(ctx.Identifier(i).getText());
            System.out.print("implements " + interfaceList);
        }
        System.out.println(" {\n");
    }

    @Override
    public void exitClassDeclaration(MiniJavaParser.ClassDeclarationContext ctx) {
        tabCount--;
        repeatTab(tabCount);
        System.out.println("}");
    }

    @Override
    public void enterInterfaceDeclaration(MiniJavaParser.InterfaceDeclarationContext ctx) {
        repeatTab(tabCount);
        tabCount++;
        System.out.println("interface " + ctx.Identifier().getText() + " {");
    }

    @Override
    public void exitInterfaceDeclaration(MiniJavaParser.InterfaceDeclarationContext ctx) {
        tabCount--;
        repeatTab(tabCount);
        System.out.println("}");
    }

    @Override
    public void enterInterfaceMethodDeclaration(MiniJavaParser.InterfaceMethodDeclarationContext ctx) {

    }

    @Override
    public void exitInterfaceMethodDeclaration(MiniJavaParser.InterfaceMethodDeclarationContext ctx) {

    }

    @Override
    public void enterFieldDeclaration(MiniJavaParser.FieldDeclarationContext ctx) {
        String fieldType = ctx.type().getText();
        if (ctx.accessModifier() != null)
            printWord(ctx.accessModifier().getText());
        if (ctx.Final() != null)
            printWord(ctx.Final().getText());
        if (fieldType.equals("number"))
            printWord("int");
        else
            printWord(ctx.type().getText());
        printWord(ctx.Identifier().getText());
    }

    @Override
    public void exitFieldDeclaration(MiniJavaParser.FieldDeclarationContext ctx) {

    }

    @Override
    public void enterLocalDeclaration(MiniJavaParser.LocalDeclarationContext ctx) {
        repeatTab(tabCount);
        System.out.println(ctx.type() + " " + ctx.Identifier() + ";");
    }

    @Override
    public void exitLocalDeclaration(MiniJavaParser.LocalDeclarationContext ctx) {
        // No exit
    }

    @Override
    public void enterMethodDeclaration(MiniJavaParser.MethodDeclarationContext ctx) {

    }

    @Override
    public void exitMethodDeclaration(MiniJavaParser.MethodDeclarationContext ctx) {

    }

    @Override
    public void enterParameterList(MiniJavaParser.ParameterListContext ctx) {
        StringBuilder paramList = new StringBuilder(ctx.parameter(0).getText());
        if (ctx.parameter(1) != null)
            for (int i = 1; i < ctx.parameter().size(); i++)
                paramList.append(",").append(ctx.parameter(i).getText());
        System.out.print(paramList);
    }

    @Override
    public void exitParameterList(MiniJavaParser.ParameterListContext ctx) {
        // No exit
    }

    @Override
    public void enterParameter(MiniJavaParser.ParameterContext ctx) {
        System.out.print(ctx.type() + " " + ctx.Identifier() + ";");
    }

    @Override
    public void exitParameter(MiniJavaParser.ParameterContext ctx) {
        // No exit
    }

    @Override
    public void enterMethodBody(MiniJavaParser.MethodBodyContext ctx) {
        if (!ctx.statement().isEmpty()) {
            for (int i = 0; i < ctx.statement().size(); i++) {
                repeatTab(tabCount);
                System.out.println(ctx.statement(i));
            }
        }
        if (ctx.getText().contains("return")) {
            repeatTab(tabCount);
            System.out.println("return " + ctx.expression() + ";");
        }
    }

    @Override
    public void exitMethodBody(MiniJavaParser.MethodBodyContext ctx) {
        // No exit
    }

    @Override
    public void enterType(MiniJavaParser.TypeContext ctx) {
        String type;
        if (ctx.javaType() != null)
            type = ctx.javaType().getText();
        else
            type = ctx.Identifier().getText();

        if (ctx.getText().contains("["))
            System.out.print(type + "[]");
        else
            System.out.print(type);
    }

    @Override
    public void exitType(MiniJavaParser.TypeContext ctx) {
        // No exit
    }

    @Override
    public void enterBooleanType(MiniJavaParser.BooleanTypeContext ctx) {
        System.out.print("boolean");
    }

    @Override
    public void exitBooleanType(MiniJavaParser.BooleanTypeContext ctx) {
        // No exit
    }

    @Override
    public void enterReturnType(MiniJavaParser.ReturnTypeContext ctx) {
        if (ctx.getText().contains("void"))
            System.out.print("void");
        else if (ctx.type() != null)
            System.out.print(ctx.type().getText());
    }

    @Override
    public void exitReturnType(MiniJavaParser.ReturnTypeContext ctx) {
        // No exit
    }

    @Override
    public void enterAccessModifier(MiniJavaParser.AccessModifierContext ctx) {
        if (ctx.getText().contains("private"))
            System.out.print("private");
        else if (ctx.getText().contains("public"))
            System.out.print("public");
    }

    @Override
    public void exitAccessModifier(MiniJavaParser.AccessModifierContext ctx) {
        // No exit
    }

    @Override
    public void enterNestedStatement(MiniJavaParser.NestedStatementContext ctx) {

    }

    @Override
    public void exitNestedStatement(MiniJavaParser.NestedStatementContext ctx) {

    }

    @Override
    public void enterIfElseStatement(MiniJavaParser.IfElseStatementContext ctx) {

    }

    @Override
    public void exitIfElseStatement(MiniJavaParser.IfElseStatementContext ctx) {
    }

    @Override
    public void enterWhileStatement(MiniJavaParser.WhileStatementContext ctx) {
    }

    @Override
    public void exitWhileStatement(MiniJavaParser.WhileStatementContext ctx) {

    }

    @Override
    public void enterPrintStatement(MiniJavaParser.PrintStatementContext ctx) {

    }

    @Override
    public void exitPrintStatement(MiniJavaParser.PrintStatementContext ctx) {

    }

    @Override
    public void enterVariableAssignmentStatement(MiniJavaParser.VariableAssignmentStatementContext ctx) {
        repeatTab(tabCount);
        System.out.println(ctx.expression(0) + " = " + ctx.expression(1) + ";");
    }

    @Override
    public void exitVariableAssignmentStatement(MiniJavaParser.VariableAssignmentStatementContext ctx) {
        // No exit
    }

    @Override
    public void enterArrayAssignmentStatement(MiniJavaParser.ArrayAssignmentStatementContext ctx) {
        repeatTab(tabCount);
        System.out.println(ctx.Identifier() + "[" + ctx.expression(0) + "] = " + ctx.expression(1) + ";");
    }

    @Override
    public void exitArrayAssignmentStatement(MiniJavaParser.ArrayAssignmentStatementContext ctx) {
        // No exit
    }

    @Override
    public void enterLocalVarDeclaration(MiniJavaParser.LocalVarDeclarationContext ctx) {
        repeatTab(tabCount);
        System.out.println(ctx.localDeclaration());
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

    }

    @Override
    public void exitIfBlock(MiniJavaParser.IfBlockContext ctx) {

    }

    @Override
    public void enterElseBlock(MiniJavaParser.ElseBlockContext ctx) {

    }

    @Override
    public void exitElseBlock(MiniJavaParser.ElseBlockContext ctx) {

    }

    @Override
    public void enterWhileBlock(MiniJavaParser.WhileBlockContext ctx) {

    }

    @Override
    public void exitWhileBlock(MiniJavaParser.WhileBlockContext ctx) {

    }

    @Override
    public void enterLtExpression(MiniJavaParser.LtExpressionContext ctx) {

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
        String type = ctx.Identifier().getText();
        if (ctx.getText().contains("number"))
            type = "number";
        else if (ctx.getText().contains("boolean"))
            type = "boolean";

        System.out.print("new " + type + "[" + ctx.expression().getText() + "]");
    }

    @Override
    public void exitArrayInstantiationExpression(MiniJavaParser.ArrayInstantiationExpressionContext ctx) {
        // No exit
    }

    @Override
    public void enterPowExpression(MiniJavaParser.PowExpressionContext ctx) {

    }

    @Override
    public void exitPowExpression(MiniJavaParser.PowExpressionContext ctx) {

    }

    @Override
    public void enterIdentifierExpression(MiniJavaParser.IdentifierExpressionContext ctx) {
        System.out.print(ctx.Identifier());
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
        System.out.print("!" + ctx.expression().getText());
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

    }

    @Override
    public void exitParenExpression(MiniJavaParser.ParenExpressionContext ctx) {

    }

    @Override
    public void enterIntLitExpression(MiniJavaParser.IntLitExpressionContext ctx) {

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

    }

    @Override
    public void exitNullLitExpression(MiniJavaParser.NullLitExpressionContext ctx) {

    }

    @Override
    public void enterAndExpression(MiniJavaParser.AndExpressionContext ctx) {

    }

    @Override
    public void exitAndExpression(MiniJavaParser.AndExpressionContext ctx) {

    }

    @Override
    public void enterArrayAccessExpression(MiniJavaParser.ArrayAccessExpressionContext ctx) {

    }

    @Override
    public void exitArrayAccessExpression(MiniJavaParser.ArrayAccessExpressionContext ctx) {

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

    }

    @Override
    public void exitArrayLengthExpression(MiniJavaParser.ArrayLengthExpressionContext ctx) {

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
