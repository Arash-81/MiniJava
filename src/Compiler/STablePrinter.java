package Compiler;

import gen.MiniJavaListener;
import gen.MiniJavaParser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

public class STablePrinter implements MiniJavaListener {
    SymbolTable parent;

    public static String getAccModText(String acc) {
        if (acc.equals("public"))
            return "ACCESS_MODIFIER_PUBLIC";
        else if (acc.equals("private"))
            return "ACCESS_MODIFIER_PRIVATE";

        return "";
    }

    @Override
    public void enterProgram(MiniJavaParser.ProgramContext ctx) {
        parent = new SymbolTable(null, "program", "root", ctx.getStart().getLine(), 0);
    }

    @Override
    public void exitProgram(MiniJavaParser.ProgramContext ctx) {
        while (!parent.type.equals("root"))
            parent = parent.parent;

        parent.print();
    }

    @Override
    public void enterMainClass(MiniJavaParser.MainClassContext ctx) {
        while (!parent.type.equals("root"))
            parent = parent.parent;

        parent.insert("Class_main", "Class: (name: main)");
        parent = new SymbolTable(parent, "main", "class", ctx.getStart().getLine(), 1);
    }

    @Override
    public void exitMainClass(MiniJavaParser.MainClassContext ctx) {

    }

    @Override
    public void enterMainMethod(MiniJavaParser.MainMethodContext ctx) {
        parent.insert("Method_main", "Method: (name: main) (return type: void) (accessModifier: public)");
        parent = new SymbolTable(parent, "main", "method", ctx.getStart().getLine(), 1);
    }

    @Override
    public void exitMainMethod(MiniJavaParser.MainMethodContext ctx) {

    }

    @Override
    public void enterClassDeclaration(MiniJavaParser.ClassDeclarationContext ctx) {
        while (!parent.type.equals("root"))
            parent = parent.parent;

        StringBuilder classBuild = new StringBuilder();
        classBuild.append("Class: (name: ").append(ctx.className.getText()).append(")");

        if (ctx.getText().contains("inherits"))
            classBuild.append(" (extends: ").append(ctx.Identifier(1).getText()).append(")");

        if (ctx.getText().contains("implements")) {
            classBuild.append(" (implements ");
            for (int i = 2; i < ctx.Identifier().size(); i++) {
                if (i != 2)
                    classBuild.append(", ");
                classBuild.append(ctx.Identifier(i).getText());
            }
            classBuild.append(")");
        }

        parent.insert("Class_" + ctx.className.getText(), classBuild.toString());
        parent = new SymbolTable(parent, ctx.className.getText(), "class", ctx.getStart().getLine(), 1);
    }

    @Override
    public void exitClassDeclaration(MiniJavaParser.ClassDeclarationContext ctx) {

    }

    @Override
    public void enterInterfaceDeclaration(MiniJavaParser.InterfaceDeclarationContext ctx) {
        while (!parent.type.equals("root"))
            parent = parent.parent;
        parent.insert("Interface_" + ctx.Identifier().getText(), "Interface: (name: " + ctx.Identifier().getText() + ")");
        parent = new SymbolTable(parent, ctx.Identifier().getText(), "interface", ctx.getStart().getLine(), 1);
    }

    @Override
    public void exitInterfaceDeclaration(MiniJavaParser.InterfaceDeclarationContext ctx) {

    }

    @Override
    public void enterInterfaceMethodDeclaration(MiniJavaParser.InterfaceMethodDeclarationContext ctx) {
        if (ctx.parameterList() != null) {
            StringBuilder params = new StringBuilder();
            for (int i = 0; i < ctx.parameterList().parameter().size(); i++) {
                var param = ctx.parameterList().parameter(i);
                if (i != 0)
                    params.append(", ");
                params.append("[").append(param.type().getText()).append(" , index: ").append(i).append("]");
            }
            String paramsRes = params.toString();

            if (ctx.accessModifier() != null)
                parent.insert("Method_" + ctx.Identifier().getText(), "Method: (name: " + ctx.Identifier().getText() + ") (returnType: " + ctx.returnType().getText() + ") (accessModifier: " + getAccModText(ctx.accessModifier().getText()) + ") (parametersType: " + paramsRes + ")");
            else
                parent.insert("Method_" + ctx.Identifier().getText(), "Method: (name: " + ctx.Identifier().getText() + ") (returnType: " + ctx.returnType().getText() + ") (parametersType: " + paramsRes + ")");
            parent = new SymbolTable(parent, ctx.Identifier().getText(), "method", ctx.getStart().getLine(), 1);
        } else {
            parent.insert("Method_" + ctx.Identifier().getText(), "Method: (name: " + ctx.Identifier().getText() + ") (returnType: " + ctx.returnType().getText() + ") (accessModifier: " + ctx.accessModifier().getText() + ")");
            parent = new SymbolTable(parent, ctx.Identifier().getText(), "method", ctx.getStart().getLine(), 1);
        }
    }

    @Override
    public void exitInterfaceMethodDeclaration(MiniJavaParser.InterfaceMethodDeclarationContext ctx) {

    }

    @Override
    public void enterFieldDeclaration(MiniJavaParser.FieldDeclarationContext ctx) {
        parent.insert("var_" + ctx.Identifier().getText(), "Field: (name: " + ctx.Identifier().getText() + ") (type: " + ctx.type().getText() + ") (accessModifier: " + getAccModText(ctx.accessModifier().getText()) + ")");
    }

    @Override
    public void exitFieldDeclaration(MiniJavaParser.FieldDeclarationContext ctx) {

    }

    @Override
    public void enterLocalDeclaration(MiniJavaParser.LocalDeclarationContext ctx) {
        parent.insert("var_" + ctx.Identifier().getText(), "LocalVar: (name: " + ctx.Identifier().getText() + ") (type: " + ctx.type().getText() + ")");
    }

    @Override
    public void exitLocalDeclaration(MiniJavaParser.LocalDeclarationContext ctx) {

    }

    @Override
    public void enterMethodDeclaration(MiniJavaParser.MethodDeclarationContext ctx) {
        while (!parent.type.contains("class"))
            parent = parent.parent;

        if (ctx.parameterList() != null) {
            StringBuilder params = new StringBuilder();
            for (int i = 0; i < ctx.parameterList().parameter().size(); i++) {
                var param = ctx.parameterList().parameter(i);
                if (i != 0)
                    params.append(", ");
                params.append("[").append(param.type().getText()).append(" , index: ").append(i).append("]");
            }
            String paramsRes = params.toString();

            parent.insert("Method_" + ctx.Identifier().getText(), "Method: (name: " + ctx.Identifier().getText() + ") (returnType: " + ctx.returnType().getText() + ") (accessModifier: " + getAccModText(ctx.accessModifier().getText()) + ") (parametersType: " + paramsRes + ")");
            parent = new SymbolTable(parent, ctx.Identifier().getText(), "method", ctx.getStart().getLine(), 1);
        } else {
            parent.insert("Method_" + ctx.Identifier().getText(), "Method: (name: " + ctx.Identifier().getText() + ") (returnType: " + ctx.returnType().getText() + ") (accessModifier: " + ctx.accessModifier().getText() + ")");
            parent = new SymbolTable(parent, ctx.Identifier().getText(), "method", ctx.getStart().getLine(), 1);
        }
    }

    @Override
    public void exitMethodDeclaration(MiniJavaParser.MethodDeclarationContext ctx) {

    }

    @Override
    public void enterParameterList(MiniJavaParser.ParameterListContext ctx) {

    }

    @Override
    public void exitParameterList(MiniJavaParser.ParameterListContext ctx) {

    }

    @Override
    public void enterParameter(MiniJavaParser.ParameterContext ctx) {
        while (!parent.type.contains("method"))
            parent = parent.parent;
        parent.insert("var_" + ctx.Identifier().getText(), "Parameter: " + "(name: " + ctx.Identifier().getText() + ") (type: " + ctx.type().getText() + ")");
    }

    @Override
    public void exitParameter(MiniJavaParser.ParameterContext ctx) {

    }

    @Override
    public void enterMethodBody(MiniJavaParser.MethodBodyContext ctx) {

    }

    @Override
    public void exitMethodBody(MiniJavaParser.MethodBodyContext ctx) {

    }

    @Override
    public void enterType(MiniJavaParser.TypeContext ctx) {

    }

    @Override
    public void exitType(MiniJavaParser.TypeContext ctx) {

    }

    @Override
    public void enterBooleanType(MiniJavaParser.BooleanTypeContext ctx) {

    }

    @Override
    public void exitBooleanType(MiniJavaParser.BooleanTypeContext ctx) {

    }

    @Override
    public void enterReturnType(MiniJavaParser.ReturnTypeContext ctx) {

    }

    @Override
    public void exitReturnType(MiniJavaParser.ReturnTypeContext ctx) {

    }

    @Override
    public void enterAccessModifier(MiniJavaParser.AccessModifierContext ctx) {

    }

    @Override
    public void exitAccessModifier(MiniJavaParser.AccessModifierContext ctx) {

    }

    @Override
    public void enterNestedStatement(MiniJavaParser.NestedStatementContext ctx) {
//        while (!parent.type.contains("method"))
//            parent = parent.parent;
//        parent = new SymbolTable(parent, "nested", "nested", ctx.getStart().getLine(), 2);
    }

    @Override
    public void exitNestedStatement(MiniJavaParser.NestedStatementContext ctx) {

    }

    @Override
    public void enterIfElseStatement(MiniJavaParser.IfElseStatementContext ctx) {
        while (!parent.type.contains("method"))
            parent = parent.parent;
        parent = new SymbolTable(parent, "if", "if", ctx.getStart().getLine(), 2);
    }

    @Override
    public void exitIfElseStatement(MiniJavaParser.IfElseStatementContext ctx) {

    }

    @Override
    public void enterWhileStatement(MiniJavaParser.WhileStatementContext ctx) {
        while (!parent.type.contains("method"))
            parent = parent.parent;
        parent = new SymbolTable(parent, "while", "while", ctx.getStart().getLine(), 2);
    }

    @Override
    public void exitWhileStatement(MiniJavaParser.WhileStatementContext ctx) {

    }

    @Override
    public void enterPrintStatement(MiniJavaParser.PrintStatementContext ctx) {
        parent.insert("print", "Print: " + "(value: " + ctx.expression().getText() + ")");
    }

    @Override
    public void exitPrintStatement(MiniJavaParser.PrintStatementContext ctx) {

    }

    @Override
    public void enterVariableAssignmentStatement(MiniJavaParser.VariableAssignmentStatementContext ctx) {
        parent.insert("var_" + ctx.expression(0).getText(), "VariableAssignment: " + "(target: " + ctx.expression(0).getText() + ") (value: " + ctx.expression(1).getText() + ")");
    }

    @Override
    public void exitVariableAssignmentStatement(MiniJavaParser.VariableAssignmentStatementContext ctx) {

    }

    @Override
    public void enterArrayAssignmentStatement(MiniJavaParser.ArrayAssignmentStatementContext ctx) {
        parent.insert("array_" + ctx.Identifier().getText(), "ArrayAssignment: " + "(name: " + ctx.Identifier().getText() + ")" + "(index: " + ctx.expression(0).getText() + ")" + " (type: " + ctx.expression(1).getText() + ")");
    }

    @Override
    public void exitArrayAssignmentStatement(MiniJavaParser.ArrayAssignmentStatementContext ctx) {

    }

    @Override
    public void enterLocalVarDeclaration(MiniJavaParser.LocalVarDeclarationContext ctx) {
        parent.insert("var_" + ctx.localDeclaration().Identifier().getText(), "LocalVar: (name: " + ctx.localDeclaration().Identifier().getText() + ") (type: " + ctx.localDeclaration().type().getText() + ")");
    }

    @Override
    public void exitLocalVarDeclaration(MiniJavaParser.LocalVarDeclarationContext ctx) {

    }

    @Override
    public void enterExpressioncall(MiniJavaParser.ExpressioncallContext ctx) {
        parent.insert("expCall_" + ctx.expression().getText(), "ExpressionCall: " + "(value: " + ctx.expression().getText() + ")");
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

    }

    @Override
    public void exitObjectInstantiationExpression(MiniJavaParser.ObjectInstantiationExpressionContext ctx) {

    }

    @Override
    public void enterArrayInstantiationExpression(MiniJavaParser.ArrayInstantiationExpressionContext ctx) {

    }

    @Override
    public void exitArrayInstantiationExpression(MiniJavaParser.ArrayInstantiationExpressionContext ctx) {

    }

    @Override
    public void enterPowExpression(MiniJavaParser.PowExpressionContext ctx) {

    }

    @Override
    public void exitPowExpression(MiniJavaParser.PowExpressionContext ctx) {

    }

    @Override
    public void enterIdentifierExpression(MiniJavaParser.IdentifierExpressionContext ctx) {

    }

    @Override
    public void exitIdentifierExpression(MiniJavaParser.IdentifierExpressionContext ctx) {

    }

    @Override
    public void enterMethodCallExpression(MiniJavaParser.MethodCallExpressionContext ctx) {

    }

    @Override
    public void exitMethodCallExpression(MiniJavaParser.MethodCallExpressionContext ctx) {

    }

    @Override
    public void enterNotExpression(MiniJavaParser.NotExpressionContext ctx) {

    }

    @Override
    public void exitNotExpression(MiniJavaParser.NotExpressionContext ctx) {

    }

    @Override
    public void enterBooleanLitExpression(MiniJavaParser.BooleanLitExpressionContext ctx) {

    }

    @Override
    public void exitBooleanLitExpression(MiniJavaParser.BooleanLitExpressionContext ctx) {

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
        parent.insert("arrayAcess_" + ctx.expression(0).getText(), "ArrayAccess: " + "(name: " + ctx.expression(0).getText() + ")" + " (index: " + ctx.expression(1).getText() + ")");
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
