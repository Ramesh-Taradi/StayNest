<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login - StayNest</title>
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>

<div class="form-container">
    <h1>Login</h1>

    <form action="/login" method="post">
        <div class="form-group">
            <label>Email</label>
            <input type="email" name="email" class="form-input" required>
        </div>

        <div class="form-group">
            <label>Password</label>
            <input type="password" name="password" class="form-input" required>
        </div>

        <button type="submit" class="button">Login</button>
    </form>

    <br>
    <p>
        Don't have an account?
        <a href="/register">Register</a>
    </p>
</div>

</body>
</html>
