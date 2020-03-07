public class Body {
    /* Helper methods to do the calculation. */
    public double xxPos;
    public double yyPos;
    public double xxVel;
    public double yyVel;
    public double mass;
    public String imgFileName;
    public static double G = 6.67e-11;


    public Body(double xP, double yP, double xV,
                double yV, double m, String img) {
        /* Constructor of a planet */
        this.xxPos = xP;
        this.yyPos = yP;
        this.xxVel = xV;
        this.yyVel = yV;
        this.mass = m;
        this.imgFileName = img;
    }


    public Body(Body b) {
        /* Constructor of a planet copy */
        this(b.xxPos, b.yyPos, b.xxVel, b.yyVel, b.mass, b.imgFileName);
    }

    public double calcDistance(Body b) {
        /* Calculate the distance between two objects */
        double deltaX = this.xxPos - b.xxPos;
        double deltaY = this.yyPos - b.yyPos;
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

    public double calcForceExertedBy(Body b) {
        /* Calculate the force between two objects */
        double distance = this.calcDistance(b);
        return G * this.mass * b.mass / (distance * distance);
    }

    public double calcForceExertedByX(Body b) {
        /* Calculate the X force of the object. */
        double force = this.calcForceExertedBy(b);
        double distance = this.calcDistance(b);
        return force * (b.xxPos - this.xxPos) / distance;
    }

    public double calcForceExertedByY(Body b) {
        /* Calculate the Y force of the object. */
        double force = this.calcForceExertedBy(b);
        double distance = this.calcDistance(b);
        return force * (b.yyPos - this.yyPos) / distance;
    }

    public double calcNetForceExertedByX(Body[] allBodys) {
        /* Calculate the sum X force of all the objects. */
        double ForceSumX = 0;
        for (Body b : allBodys) {
            if (this.equals(b)) {
                continue;
            } else {
                ForceSumX = ForceSumX + this.calcForceExertedByX(b);
            }
        }
        return ForceSumX;
    }

    public double calcNetForceExertedByY(Body[] allBodys) {
        /* Calculate the sum Y force of all the objects. */
        double ForceSumY = 0;
        for (Body b : allBodys) {
            if (this.equals(b)) {
                continue;
            } else {
                ForceSumY = ForceSumY + this.calcForceExertedByY(b);
            }
        }
        return ForceSumY;
    }

    public void update(double t, double forceX, double forceY) {
        /** Update the acceleration of objects */
        double accelerationX = forceX / this.mass;
        double accelerationY = forceY / this.mass;
        this.xxVel = this.xxVel + accelerationX * t;
        this.yyVel = this.yyVel + accelerationY * t;
        this.xxPos = this.xxPos + this.xxVel * t;
        this.yyPos = this.yyPos + this.yyVel * t;

    }

    public void draw() {
        /** Draw itself at its approriate position. */
        String imgLocation = "images/" + imgFileName;
        StdDraw.picture(xxPos, yyPos, imgLocation);
    }
}