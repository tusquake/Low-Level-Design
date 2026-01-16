import java.util.ArrayList;
import java.util.List;

interface ShapeVisitor{
    void visit(Circle circle);
    void visit(Square square);
}

interface Shape{
    void accept(ShapeVisitor shapeVisitor);
}

class Circle implements Shape{
    private double radius;

    public Circle(double radius){
        this.radius = radius;
    }

    public double getRadius(){
        return radius;
    }


    @Override
    public void accept(ShapeVisitor shapeVisitor) {
        shapeVisitor.visit(this);
    }
}

class Square implements Shape{
    private double sides;

    public Square(double sides){
        this.sides = sides;
    }

    public double getSides(){
        return sides;
    }


    @Override
    public void accept(ShapeVisitor shapeVisitor) {
        shapeVisitor.visit(this);
    }
}

class AreaCalculatorVisitor implements ShapeVisitor{
    @Override
    public void visit(Circle circle) {
        double radius = circle.getRadius();
        double area = 3.14 * radius * radius;
        System.out.println("Circle area: " + area);
    }

    @Override
    public void visit(Square square) {
        double sides = square.getSides();
        double area = sides * sides;
        System.out.println("Sqaure area: " + area);
    }
}

class PerimeterCalculatorVisitor implements ShapeVisitor{
    @Override
    public void visit(Circle circle) {
        double radius = circle.getRadius();
        double perimeter = 2 * 3.14 * radius;
        System.out.println("Circle perimeter: " + perimeter);
    }

    @Override
    public void visit(Square square) {
        double sides = square.getSides();
        double perimeter = 4 * sides;
        System.out.println("Sqaure perimeter: " + perimeter);
    }
}

class XMLExportVisitor implements ShapeVisitor{

    @Override
    public void visit(Circle circle) {
        System.out.println("<circle radius='" + circle.getRadius() + "'/>");
    }

    @Override
    public void visit(Square square) {
        System.out.println("<sqaure radius='" + square.getSides() + "'/>");
    }
}

public class VisitorDemo{
    public static void main(String[] args) throws InterruptedException {
        List<Shape> shapes = new ArrayList<>();
        shapes.add(new Circle(3.5));
        shapes.add(new Square(4.5));

        System.out.println("---------Area Calculator----------------");
        ShapeVisitor areaCalculator = new AreaCalculatorVisitor();
        for(Shape shape:shapes){
            shape.accept(areaCalculator);
        }

        Thread.sleep(2000);

        System.out.println("---------Perimeter Calculator----------------");
        ShapeVisitor perimeterCalculator = new PerimeterCalculatorVisitor();
        for(Shape shape:shapes){
            shape.accept(perimeterCalculator);
        }

        Thread.sleep(2000);

        System.out.println("---------XML Exporter---------------");
        ShapeVisitor xmlExporter = new XMLExportVisitor();
        for(Shape shape:shapes){
            shape.accept(xmlExporter);
        }
    }
}

