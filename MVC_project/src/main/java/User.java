public class User {
    private final String name;
    private final int age;
    private double accountBalance;

    public User(String name, int age, double accountBalance) {
        this.name = name;
        this.age = age;
        this.accountBalance = accountBalance;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(double accountBalance) {
        this.accountBalance = accountBalance;
    }
}
