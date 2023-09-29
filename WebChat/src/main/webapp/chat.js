$(document).ready(function () {
    const signin_email = localStorage.getItem("signin_email");
    var signin_id;
    if (signin_email == null) {
        window.location.href = 'http://localhost:8080/WebChat/';
    }

    $(".logout").click(function () {
        console.log("Logout")
        localStorage.removeItem("signin_email");
        $.setStatus(signin_id)
        window.location.href = 'http://localhost:8080/WebChat/';
    });

    $.ajax({
        url: "member?email=" + signin_email + '&type=true',
        method: "PUT",
        dataType: "json",
        success: function (response) {
            console.log(response)
        },
        error: function () {
            console.log("FullDetails Error")
        }
    });

    $.ajax({
        url: "member?key=email&email=" + signin_email,
        method: "GET",
        dataType: "json",
        success: function (response) {
            var member = response.members;
            member.forEach(function (user) {
                var img = $('<img>').attr("src", "data:image/jpeg;base64," + user.image);
                $(".users header .img").append(img)
                $(".users header .details span").html(user.first_name + " " + user.last_name);
                signin_id = user.unique_id;
                sessionStorage.setItem("member_unique_id", user.unique_id);
                $.init_ws()
                $.lastMessage(user.unique_id)
            });
        },
        error: function () {
            console.log("OneDetails Error")
        }
    });

    $.lastMessage = function (id) {
        var data = { id: id, key: 'lastmsg' };
        $.get("message", data, function (response) {
            var msg = response.message;
            msg.forEach(function (data) {
                var lastmsg = $.lastmsg(data.message)
                $("#" + data.incoming_id + " p").html(lastmsg);
                $("#" + data.outgoing_id + " p").html(lastmsg);
            });
        });
    }

    $.lastmsg = function (lastmsg) {
        if (lastmsg.length > 25) {
            lastmsg = lastmsg.substring(0, 23) + '...';
        }
        return lastmsg
    }

    $.ajax({
        url: "member?key=full&email=" + signin_email,
        method: "GET",
        dataType: "json",
        success: function (response) {
            var member = response.members;
            var flag = true;
            member.forEach(function (user) {
                var img = $('<img>').attr("src", "data:image/jpeg;base64," + user.image);
                var new_user = $('<a>').attr('id', user.unique_id)
                var div_tag = $('<div>').attr('class', 'content')
                div_tag.append(img)
                var details = $('<div class="details"><span>' + user.first_name + " " + user.last_name + '</span><p>Start chating...</p></div>');
                div_tag.append(details)
                new_user.append(div_tag)
                new_user.click(function () {
                    localStorage.setItem("unique_id", $(this).attr("id"));
                    $(".blank-area").css({ "display": "none" })
                    $(".contant-area").css({ "display": "block" })
                    chatdetails()
                });
                $(".users-list").append(new_user);
                flag = false;
            });

            if (flag) {
                $(".list").css({ "display": "block" });
            }
        },
        error: function () {
            console.log("FullDetails Error")
        }
    });




    $(".search input").keyup(function () {
        $(".users-list").empty();
        $.ajax({
            url: "member?key=search&email=" + signin_email + '&name=' + $(this).val(),
            method: "GET",
            dataType: "json",
            success: function (response) {
                var member = response.members;
                var flag = true;
                member.forEach(function (user) {
                    var img = $('<img>').attr("src", "data:image/jpeg;base64," + user.image);
                    var new_user = $('<a>').attr('id', user.unique_id)
                    var div_tag = $('<div>').attr('class', 'content')
                    div_tag.append(img)
                    var details = $('<div class="details"><span>' + user.first_name + " " + user.last_name + '</span><p>Start chating...</p></div>');
                    div_tag.append(details)
                    new_user.append(div_tag)
                    new_user.click(function () {
                        localStorage.setItem("unique_id", $(this).attr("id"));
                        $(".blank-area").css({ "display": "none" })
                        $(".contant-area").css({ "display": "block" })
                        chatdetails()
                    });
                    $(".users-list").append(new_user);
                    flag = false;
                });
                $.lastMessage(sessionStorage.getItem("member_unique_id"))

                if (flag) {
                    $(".users-list").html('<p class=\"no-list\">No Result found</p>');
                }
            },
            error: function () {
                console.log("FullDetails Error")
            }
        });
    });


    function chatdetails() {
        var unique_id = localStorage.getItem("unique_id")
        $(".chat-box").empty();
        $.ajax({
            url: "member?key=id&unique_id=" + unique_id,
            method: "GET",
            dataType: "json",
            success: function (response) {
                var member = response.members;
                member.forEach(function (user) {
                    var img = $('<img>').attr("src", "data:image/jpeg;base64," + user.image);
                    $(".contant-area header .img").html(img)
                    $(".contant-area header .details span").html(user.first_name + " " + user.last_name);
                    $(".contant-area header .details p").html('' + user.status);
                    sessionStorage.setItem("outgoing_member_unique_id", user.unique_id);
                });
                $.getMessage();
            },
            error: function () {
                console.log("FullDetails Error")
            }
        });
    }
    $.getMessage = function () {
        var incoming_id = sessionStorage.getItem("member_unique_id");
        var outgoing_id = sessionStorage.getItem("outgoing_member_unique_id");
        var data = { incoming_id: incoming_id, outgoing_id: outgoing_id, key: 'totalmsg' };
        $.get("message", data, function (response) {
            var msg = response.message;
            msg.forEach(function (data) {
                if (data.incoming_id == incoming_id) {
                    $(".chat-box").append("<div class=\"chat outgoing\"><div class=\"details\"><p>" + data.message + "</p></div></div>")
                } else {
                    $(".chat-box").append("<div class=\"chat incoming\"><div class=\"details\"><p>" + data.message + "</p></div></div>")
                }
            });
        });
    }

    $(".typing-area .button").click(function () {
        var content = $(".typing-area input").val();
        $(".typing-area input").val("")
        if (content != "") {
            $(".chat-box").append("<div class=\"chat outgoing\"><div class=\"details\"><p>" + content + "</p></div></div>")
            var incoming_id = sessionStorage.getItem("member_unique_id");
            var outgoing_id = sessionStorage.getItem("outgoing_member_unique_id");
            $("#" + outgoing_id + " .content .details p").html($.lastmsg(content))
            var data = { incoming_id: incoming_id, outgoing_id: outgoing_id, messages: content };
            $.post("message", data, function (response) {
                if (response == "Inserted") {
                    console.log("Inserted");
                }
            });
            var json = JSON.stringify({
                "to": outgoing_id,
                "content": content
            });
            ws.send(json);
        }
    });

    $.setStatus = function (id) {
        $.ajax({
            url: "member?id=" + id+'&type=false',
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

    // WebSocket
    var ws;
    var host = document.location.host;
    var pathname = document.location.pathname;
    $.init_ws = function () {
        ws = new WebSocket("ws://" + host + pathname + "/" + signin_id);
        ws.onmessage = function (event) {
            var outgoing_id = sessionStorage.getItem("outgoing_member_unique_id");
            var message = JSON.parse(event.data);
            if(signin_id == message.to){
                $("#" + message.from + " .content .details p").html($.lastmsg(message.content))
            }
            if (undefined != message.to && outgoing_id == message.from) {
                $(".chat-box").append("<div class=\"chat incoming\"><div class=\"details\"><p>" + message.content + "</p></div></div>")
            }
            if (message.to == undefined && 'Disconnected!' == message.content) {
                $.setStatus(message.from)
            }
        };
    }
});