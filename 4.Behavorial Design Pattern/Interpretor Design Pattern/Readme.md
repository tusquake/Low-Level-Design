# Interpreter Design Pattern

The Interpreter pattern is a behavioral design pattern that defines a grammatical representation for a language and provides an interpreter to interpret sentences in that language. It's used to evaluate expressions by representing grammar rules as classes and building an Abstract Syntax Tree (AST) to process structured data.

## Problem Statement

In traditional approaches, when you need to evaluate expressions or process domain-specific languages, you might hardcode the parsing logic directly into your application. This creates inflexible code that's difficult to extend or modify when grammar rules change.

### Without Interpreter Pattern

Consider a calculator that evaluates arithmetic expressions:

```java
public class Calculator {
    public int evaluate(String expression) {
        // Complex parsing and evaluation logic hardcoded
        String[] parts = expression.split(" ");
        int result = Integer.parseInt(parts[0]);
        
        for (int i = 1; i < parts.length; i += 2) {
            String operator = parts[i];
            int operand = Integer.parseInt(parts[i + 1]);
            
            if (operator.equals("+")) {
                result += operand;
            } else if (operator.equals("-")) {
                result -= operand;
            } else if (operator.equals("*")) {
                result *= operand;
            }
        }
        return result;
    }
}
```

**Issues:**
- Grammar rules are hardcoded and inflexible
- Difficult to extend with new operations
- Cannot handle complex nested expressions
- No clear separation between parsing and evaluation
- Hard to maintain and test individual grammar rules
- Cannot reuse grammar components

## Solution

The Interpreter pattern represents each grammar rule as a class. Terminal expressions (leaf nodes) represent basic elements, while non-terminal expressions (composite nodes) represent combination rules. These classes form an Abstract Syntax Tree that can be interpreted recursively.

## Architecture

```
Client → Abstract Syntax Tree → Expression Interface
                                      ↑
                         ┌────────────┴────────────┐
                         │                         │
                  TerminalExpression      NonTerminalExpression
                    (numbers)              (operations: +, -, *)
```

## Implementation

### Core Components

**Expression Interface**
```java
interface Expression {
    int interpret();
}
```

**Terminal Expression (Numbers)**
```java
class NumberExpression implements Expression {
    private int number;
    
    public NumberExpression(int number) {
        this.number = number;
    }
    
    @Override
    public int interpret() {
        return number;
    }
    
    @Override
    public String toString() {
        return String.valueOf(number);
    }
}
```

**Non-Terminal Expressions (Operations)**
```java
class AddExpression implements Expression {
    private Expression leftExpression;
    private Expression rightExpression;
    
    public AddExpression(Expression left, Expression right) {
        this.leftExpression = left;
        this.rightExpression = right;
    }
    
    @Override
    public int interpret() {
        return leftExpression.interpret() + rightExpression.interpret();
    }
    
    @Override
    public String toString() {
        return "(" + leftExpression + " + " + rightExpression + ")";
    }
}

class SubtractExpression implements Expression {
    private Expression leftExpression;
    private Expression rightExpression;
    
    public SubtractExpression(Expression left, Expression right) {
        this.leftExpression = left;
        this.rightExpression = right;
    }
    
    @Override
    public int interpret() {
        return leftExpression.interpret() - rightExpression.interpret();
    }
    
    @Override
    public String toString() {
        return "(" + leftExpression + " - " + rightExpression + ")";
    }
}

class MultiplyExpression implements Expression {
    private Expression leftExpression;
    private Expression rightExpression;
    
    public MultiplyExpression(Expression left, Expression right) {
        this.leftExpression = left;
        this.rightExpression = right;
    }
    
    @Override
    public int interpret() {
        return leftExpression.interpret() * rightExpression.interpret();
    }
    
    @Override
    public String toString() {
        return "(" + leftExpression + " * " + rightExpression + ")";
    }
}
```

**Context (Optional)**
```java
class Context {
    private Map<String, Integer> variables = new HashMap<>();
    
    public void setVariable(String name, int value) {
        variables.put(name, value);
    }
    
    public int getVariable(String name) {
        return variables.getOrDefault(name, 0);
    }
}
```

**Parser/Client**
```java
class ExpressionParser {
    
    // Parse simple expressions like "5 + 3 - 2"
    public static Expression parse(String expression) {
        String[] tokens = expression.split(" ");
        Stack<Expression> stack = new Stack<>();
        
        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i];
            
            if (isNumber(token)) {
                stack.push(new NumberExpression(Integer.parseInt(token)));
            } else if (token.equals("+")) {
                Expression left = stack.pop();
                Expression right = new NumberExpression(Integer.parseInt(tokens[++i]));
                stack.push(new AddExpression(left, right));
            } else if (token.equals("-")) {
                Expression left = stack.pop();
                Expression right = new NumberExpression(Integer.parseInt(tokens[++i]));
                stack.push(new SubtractExpression(left, right));
            } else if (token.equals("*")) {
                Expression left = stack.pop();
                Expression right = new NumberExpression(Integer.parseInt(tokens[++i]));
                stack.push(new MultiplyExpression(left, right));
            }
        }
        
        return stack.pop();
    }
    
    private static boolean isNumber(String token) {
        try {
            Integer.parseInt(token);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
```

### Usage

```java
public class InterpreterDemo {
    public static void main(String[] args) {
        // Manual AST construction
        // Represents: (5 + 3) - 2
        Expression expr1 = new SubtractExpression(
            new AddExpression(
                new NumberExpression(5),
                new NumberExpression(3)
            ),
            new NumberExpression(2)
        );
        
        System.out.println("Expression: " + expr1);
        System.out.println("Result: " + expr1.interpret());
        
        // Using parser
        Expression expr2 = ExpressionParser.parse("5 + 3 - 2");
        System.out.println("\nExpression: " + expr2);
        System.out.println("Result: " + expr2.interpret());
        
        // Complex expression: ((10 + 5) * 2) - 8
        Expression expr3 = new SubtractExpression(
            new MultiplyExpression(
                new AddExpression(
                    new NumberExpression(10),
                    new NumberExpression(5)
                ),
                new NumberExpression(2)
            ),
            new NumberExpression(8)
        );
        
        System.out.println("\nExpression: " + expr3);
        System.out.println("Result: " + expr3.interpret());
    }
}
```

## Benefits

1. **Easy to Extend**: Add new expression types without modifying existing code
2. **Grammar Representation**: Grammar rules are explicitly represented in code
3. **Separation of Concerns**: Each grammar rule is encapsulated in its own class
4. **Flexibility**: Easy to change or extend the grammar
5. **Composability**: Complex expressions can be built from simple ones
6. **Maintainability**: Each expression class has a single responsibility

## Drawbacks

1. **Class Explosion**: Each grammar rule requires a separate class
2. **Complex Grammars**: Can become difficult to maintain for large, complex grammars
3. **Performance**: Recursive interpretation can be slower than compiled approaches
4. **Limited Use Cases**: Only suitable for relatively simple languages
5. **Parser Required**: Still need to implement a parser to build the AST

## When to Use

Apply the Interpreter pattern when:

- You have a simple language or grammar to interpret
- The grammar is relatively stable and doesn't change frequently
- Efficiency is not critical (for performance-critical applications, consider compilers)
- You want to represent language rules as classes
- You need to interpret domain-specific languages (DSLs)
- The grammar is simple enough that the benefits outweigh the complexity

## Real-World Examples

1. **Regular Expressions** - Pattern matching engines (java.util.Pattern)
2. **SQL Parsers** - Database query interpretation
3. **Mathematical Expression Evaluators** - Calculator applications
4. **Scripting Languages** - Simple script interpreters
5. **Configuration Files** - Parsing and interpreting config syntax
6. **Rule Engines** - Business rule evaluation systems
7. **Compiler Front-Ends** - Parsing source code into ASTs
8. **Roman Numeral Converters** - Converting between numeral systems

## Class Diagram

```
┌─────────────────────┐
│    Expression       │
│   «interface»       │
├─────────────────────┤
│ + interpret(): int  │
└─────────────────────┘
          △
          │
    ┌─────┴──────────────────────────┐
    │                                │
┌───┴────────────────┐   ┌───────────┴──────────────┐
│ NumberExpression   │   │ OperatorExpression       │
│  (Terminal)        │   │  (Non-Terminal)          │
├────────────────────┤   ├──────────────────────────┤
│ - number: int      │   │ - left: Expression       │
│ + interpret(): int │   │ - right: Expression      │
└────────────────────┘   │ + interpret(): int       │
                         └──────────────────────────┘
                                    △
                                    │
                     ┌──────────────┼──────────────┐
                     │              │              │
              ┌──────┴──────┐ ┌────┴────┐ ┌──────┴──────┐
              │AddExpression│ │Subtract │ │Multiply     │
              │             │ │Expression│ │Expression   │
              └─────────────┘ └─────────┘ └─────────────┘
```

## Output Example

```
Expression: ((5 + 3) - 2)
Result: 6

Expression: ((5 + 3) - 2)
Result: 6

Expression: (((10 + 5) * 2) - 8)
Result: 22
```

## Variations

### Boolean Expression Interpreter

```java
interface BooleanExpression {
    boolean interpret(Context context);
}

class TerminalExpression implements BooleanExpression {
    private String data;
    
    public TerminalExpression(String data) {
        this.data = data;
    }
    
    @Override
    public boolean interpret(Context context) {
        return context.contains(data);
    }
}

class OrExpression implements BooleanExpression {
    private BooleanExpression expr1;
    private BooleanExpression expr2;
    
    public OrExpression(BooleanExpression expr1, BooleanExpression expr2) {
        this.expr1 = expr1;
        this.expr2 = expr2;
    }
    
    @Override
    public boolean interpret(Context context) {
        return expr1.interpret(context) || expr2.interpret(context);
    }
}

class AndExpression implements BooleanExpression {
    private BooleanExpression expr1;
    private BooleanExpression expr2;
    
    public AndExpression(BooleanExpression expr1, BooleanExpression expr2) {
        this.expr1 = expr1;
        this.expr2 = expr2;
    }
    
    @Override
    public boolean interpret(Context context) {
        return expr1.interpret(context) && expr2.interpret(context);
    }
}
```

### Roman Numeral Interpreter

```java
class RomanContext {
    private String input;
    private int output;
    
    public RomanContext(String input) {
        this.input = input;
        this.output = 0;
    }
    
    public String getInput() { return input; }
    public void setInput(String input) { this.input = input; }
    public int getOutput() { return output; }
    public void setOutput(int output) { this.output = output; }
}

abstract class RomanExpression {
    public void interpret(RomanContext context) {
        if (context.getInput().isEmpty()) {
            return;
        }
        
        if (context.getInput().startsWith(nine())) {
            context.setOutput(context.getOutput() + (9 * multiplier()));
            context.setInput(context.getInput().substring(2));
        } else if (context.getInput().startsWith(four())) {
            context.setOutput(context.getOutput() + (4 * multiplier()));
            context.setInput(context.getInput().substring(2));
        } else if (context.getInput().startsWith(five())) {
            context.setOutput(context.getOutput() + (5 * multiplier()));
            context.setInput(context.getInput().substring(1));
        }
        
        while (context.getInput().startsWith(one())) {
            context.setOutput(context.getOutput() + multiplier());
            context.setInput(context.getInput().substring(1));
        }
    }
    
    public abstract String one();
    public abstract String four();
    public abstract String five();
    public abstract String nine();
    public abstract int multiplier();
}

class ThousandExpression extends RomanExpression {
    public String one() { return "M"; }
    public String four() { return " "; }
    public String five() { return " "; }
    public String nine() { return " "; }
    public int multiplier() { return 1000; }
}

class HundredExpression extends RomanExpression {
    public String one() { return "C"; }
    public String four() { return "CD"; }
    public String five() { return "D"; }
    public String nine() { return "CM"; }
    public int multiplier() { return 100; }
}

// Usage
String roman = "MCMXXVIII";  // 1928
RomanContext context = new RomanContext(roman);

List<RomanExpression> tree = Arrays.asList(
    new ThousandExpression(),
    new HundredExpression(),
    new TenExpression(),
    new OneExpression()
);

for (RomanExpression exp : tree) {
    exp.interpret(context);
}

System.out.println(roman + " = " + context.getOutput());
```

### Expression with Variables

```java
class VariableExpression implements Expression {
    private String variableName;
    private Context context;
    
    public VariableExpression(String name, Context context) {
        this.variableName = name;
        this.context = context;
    }
    
    @Override
    public int interpret() {
        return context.getVariable(variableName);
    }
}

// Usage
Context context = new Context();
context.setVariable("x", 10);
context.setVariable("y", 5);

// Expression: (x + y) * 2
Expression expr = new MultiplyExpression(
    new AddExpression(
        new VariableExpression("x", context),
        new VariableExpression("y", context)
    ),
    new NumberExpression(2)
);

System.out.println("Result: " + expr.interpret());  // 30
```

## Advanced Features

### Expression Visitor for Operations

```java
interface ExpressionVisitor {
    void visit(NumberExpression expr);
    void visit(AddExpression expr);
    void visit(SubtractExpression expr);
}

class PrintVisitor implements ExpressionVisitor {
    @Override
    public void visit(NumberExpression expr) {
        System.out.print(expr.interpret());
    }
    
    @Override
    public void visit(AddExpression expr) {
        System.out.print("(");
        expr.getLeft().accept(this);
        System.out.print(" + ");
        expr.getRight().accept(this);
        System.out.print(")");
    }
    
    @Override
    public void visit(SubtractExpression expr) {
        System.out.print("(");
        expr.getLeft().accept(this);
        System.out.print(" - ");
        expr.getRight().accept(this);
        System.out.print(")");
    }
}
```

### Expression Caching

```java
class CachedExpression implements Expression {
    private Expression expression;
    private Integer cachedResult;
    
    public CachedExpression(Expression expression) {
        this.expression = expression;
    }
    
    @Override
    public int interpret() {
        if (cachedResult == null) {
            cachedResult = expression.interpret();
        }
        return cachedResult;
    }
}
```

## Comparison with Other Patterns

| Pattern | Purpose | Key Difference |
|---------|---------|----------------|
| **Interpreter** | Evaluate language expressions | Focuses on grammar and language interpretation |
| **Composite** | Tree structures | Focuses on part-whole hierarchies, not language |
| **Visitor** | Operations on object structures | Separates algorithms from object structure |
| **Strategy** | Interchangeable algorithms | Focuses on algorithm selection, not grammar |
| **Command** | Encapsulate requests | Focuses on actions, not language interpretation |

## Best Practices

1. **Keep Grammar Simple**: Pattern works best for simple, stable grammars
2. **Use Composite Pattern**: Leverage Composite for building expression trees
3. **Separate Parsing**: Keep parsing logic separate from interpretation
4. **Immutable Expressions**: Make expression objects immutable when possible
5. **Context Management**: Use Context object to maintain state during interpretation
6. **Error Handling**: Implement proper error handling for invalid expressions
7. **Testing**: Test each expression type independently and in combination
8. **Documentation**: Document the grammar rules clearly

## Common Use Cases

### SQL Query Interpreter
```java
// Simple WHERE clause interpreter
// "age > 18 AND status = active"
```

### Configuration File Parser
```java
// Parse configuration syntax
// "server.port = 8080"
```

### Mathematical Expression Evaluator
```java
// "3.14 * radius * radius"
```

### Rule Engine
```java
// Business rules
// "IF customer.age > 65 THEN discount = 0.2"
```

## Testing Example

```java
@Test
public void testSimpleAddition() {
    Expression expr = new AddExpression(
        new NumberExpression(5),
        new NumberExpression(3)
    );
    assertEquals(8, expr.interpret());
}

@Test
public void testComplexExpression() {
    // (10 + 5) * 2
    Expression expr = new MultiplyExpression(
        new AddExpression(
            new NumberExpression(10),
            new NumberExpression(5)
        ),
        new NumberExpression(2)
    );
    assertEquals(30, expr.interpret());
}

@Test
public void testParser() {
    Expression expr = ExpressionParser.parse("5 + 3 - 2");
    assertEquals(6, expr.interpret());
}
```

## Conclusion

The Interpreter pattern provides a structured way to evaluate expressions in a language by representing grammar rules as classes and building an Abstract Syntax Tree. While it's best suited for simple grammars due to potential complexity and performance concerns, it offers excellent extensibility and clear separation of grammar rules. The pattern is particularly valuable in domain-specific languages, rule engines, and expression evaluators where the grammar is relatively stable and simplicity is prioritized over performance. By leveraging the Composite pattern and recursive interpretation, it creates an elegant solution for language processing problems.