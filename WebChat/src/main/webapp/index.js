$(document).ready(function () {
    console.log("local storage" + localStorage.getItem("signin_email"));
    if (localStorage.getItem("signin_email") != null) {
        window.location.href = 'chat.html';
    }

    const passwordPattern = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z0-9])(?!.*\s).{8,15}$/;

    $("#signup-form").submit(function (e) {
        e.preventDefault();
        $(".signup-error").hide();
        var formData = new FormData(this);
        console.log("Form Data : " + formData.get('signup-first-name'))
        if (formData.get('first-name') == '' || formData.get('email') == '' || formData.get('password') == '') {
            $(".signup-error").html("Please fill the form").css({ "font-size": "15px", "color": "red", "text-align": "center", "display": "block" }).show();
        } else if (!(passwordPattern.test(formData.get('password')))) {
            $(".signup-error").html("Password don't match our requirement").css({ "font-size": "15px", "color": "red", "text-align": "center", "display": "block" }).show();
        } else {
            $.ajax({
                url: "home",
                type: "POST",
                data: formData,
                processData: false,
                contentType: false,
                success: function (response) {
                    console.log("response : " + response)
                    if ("Register" == response) {
                        $(".message").css({ "display": "block" });
                        $(".forgot-password,.signin,.signup").css({ "display": "none" });
                        $("#return-message").html("Successfully Registered").css({ "font-size": "25px", "color": "#333", "text-align": "center" });
                    } else {
                        $(".signup-error").html("The Account already exists").css({ "font-size": "15px", "color": "red", "text-align": "center", "display": "block" }).show();
                    }
                },
                error: function () {
                    alert("Error uploading image.");
                }
            });
        }
    });

    $("#signin-form").on("submit", function (event) {
        event.preventDefault();
        $(".signin-error").hide()
        var formData = new FormData(this);
        var email = formData.get('email');
        var password = formData.get('password');
        var data = { email: email, password: password };
        console.log(formData.get('email')+" "+formData.get('password'))
        if (formData.get('email') == '' || formData.get('password') == '') {
            $(".signin-error").html("Please fill the form").css({ "font-size": "15px", "color": "red", "text-align": "center", "display": "block" }).show();
        } else {
            $.get("home", data, function (response) {
                if ("false" == response) {
                    $(".signin-error").html("The Account does not exists").css({ "font-size": "15px", "color": "red", "text-align": "center", "display": "block" }).show();
                } else if ("wrong" == response) {
                    $(".signin-error").html("Your Password was Wrong").css({ "font-size": "15px", "color": "red", "text-align": "center", "display": "block" }).show();
                } else if ("true" == response) {
                    $.setStatus(email)
                    localStorage.setItem("signin_email", email);
                    window.location.href = 'chat.html';
                }
            });
        }
    });

    $.setStatus = function (email) {
        $.ajax({
            url: "member?email=" + email + '&type=true',
            method: "PUT",
            dataType: "json",
            success: function (response) {
                console.log(response)
            },
            error: function () {
                console.log("FullDetails Error")
            }
        });
    }

    $("#forgot-pwd-form").on("submit", function (event) {
        event.preventDefault();
        $(".error-txt").hide()
        var formData = new FormData(this);
        var email = formData.get('email');
        var password = formData.get('password');
        if (email == '' || password == '') {
            $(".error-txt").html("Please fill the form").css({ "font-size": "15px", "color": "red", "text-align": "center", "display": "block" }).show();
        } else if (!(passwordPattern.test(password))) {
            $(".error-txt").html("Password don't match our requirement").css({ "font-size": "15px", "color": "red", "text-align": "center", "display": "block" }).show();
        } else {
            $.ajax({
                url: "home?email=" + email + '&password=' + password,
                method: "PUT",
                dataType: "json",
                success: function (response) {
                    console.log("Response : " + response + ' ' + typeof (response))
                    if (true === response) {
                        $(".message").css({ "display": "block" });
                        $(".forgot-password,.signin,.signup").css({ "display": "none" });
                        $("#return-message").html("Successfully Updated").css({ "font-size": "25px", "color": "#333", "text-align": "center" });
                    } else {
                        $(".error-txt").html("The Account does not exists").css({ "font-size": "15px", "color": "red", "text-align": "center", "display": "block" }).show();
                    }
                },
                error: function () {
                    console.log("FullDetails Error")
                }
            });
        }
    });

    $("#sign-up").click(function () {
        $(".signin-error").hide()
        $(".signup").show();
        $(".forgot-password,.signin,.message").hide()
    });

    $("#sign-in,#sign-in-btn").click(function () {
        $(".signup-error").hide();
        $(".signin").show();
        $(".forgot-password,.signup,.message").hide()
    });

    $("#forgot-btn").click(function () {
        $(".forgot-password").show();
        $(".signin,.signup,.message").hide()
    });

});