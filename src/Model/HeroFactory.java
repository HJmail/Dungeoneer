package model;

public final class HeroFactory {

    private HeroFactory() {
        // Prevent instantiation
    }

    public static Hero createHero(final String theType, final String theName) {
        switch (theType.toLowerCase()) {

            case "warrior":
                return new Warrior(theName);

            case "thief":
                return new Thief(theName);

            case "priestess":
                return new Priestess(theName);

            default:
                System.err.println("Unknown hero type: " + theType + ". Defaulting to Warrior.");
                return new Warrior(theName);
        }
    }
}