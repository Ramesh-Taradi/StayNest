<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Register - StayNest</title>
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>

<div class="form-container">
    <h1>Create Account</h1>

    <form action="/register" method="post">
        <div class="form-group">
            <label>Name</label>
            <input type="text" name="name" class="form-input" required>
        </div>

        <div class="form-group">
            <label>Email</label>
            <input type="email" name="email" class="form-input" required>
        </div>

        <div class="form-group">
            <label>Password</label>
            <input type="password" name="password" class="form-input" required>
        </div>

        <button type="submit" class="button">Register</button>
    </form>

    <br>
    <p>
        Already have an account?
        <a href="/login">Login</a>
    </p>
</div>

</body>
</html>
