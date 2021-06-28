function post() {
    var questionId = $("#question_id").val();
    var content = $("#comment_content").val();
    comment2Target(questionId, 1, content);
}

function sethover(e){
    var commentId = e.getAttribute("data-id1");
    var comments = $("#commentlike-" + commentId);
    var like=$(comments.get(0));
    console.log(like);

    $.ajax({
        type: "POST",
        url: "/commentlike",
        contentType: 'application/json',
        data: JSON.stringify({
            "commentId": commentId,
        }),

        success: function (response) {
                if (response.code == 2003) {
                    if (confirm(response.message)) {
                        window.open("https://github.com/login/oauth/authorize?client_id=3783cac01f9c1383eb5" +
                            "c&redirect_uri=http://localhost:8090/callback&scope=user%20public_repo&state=2"
                        )
                        window.localStorage.setItem("closable", true);
                    }
                } else{
                    let likeCounts = response.likeCounts;
                    e.classList.add("active");
                    like.html(likeCounts);
                //    <span class="glyphicon glyphicon-thumbs-up like-hp icon active" id="commentlike-12" data-id1="12" onclick="sethover(this)">67</span>
                }
            console.log(response);
        },
        dataType: "json"
    });


}
function comment2Target(targetId, type, content) {
    $.ajax({
        type: "POST",
        url: "/comment",
        contentType: 'application/json',
        data: JSON.stringify({
            "parentId": targetId,
            "content": content,
            "type": type
        }),

        success: function (response) {
            if (response.code == 200) {
                window.location.reload();
            } else {
                if (response.code == 2003) {
                    if (confirm(response.message)) {
                        window.open("https://github.com/login/oauth/authorize?client_id=3783cac01f9c1383eb5" +
                            "c&redirect_uri=http://localhost:8090/callback&scope=user%20public_repo&state=2")
                        window.localStorage.setItem("closable", true);
                    }
                } else
                    alert(response.message);
            }
            console.log(response);
        },
        dataType: "json"
    });
}

function comment(e) {
    var commentId = e.getAttribute("data-id");
    var content = $("#input-" + commentId).val();
    comment2Target(commentId, 2, content);
}

function collapseComments(e) {
    var id = e.getAttribute("data-id");
    var comments = $("#comment-" + id);

    // 获取一下二级评论的展开状态
    var collapse = e.getAttribute("data-collapse");
    if (collapse) {
        // 折叠二级评论
        comments.removeClass("in");
        e.removeAttribute("data-collapse");
        e.classList.remove("active");
    } else {
        var subCommentContainer = $("#comment-" + id);
        if (subCommentContainer.children().length != 1) {
            //展开二级评论
            comments.addClass("in");
            // 标记二级评论展开状态
            e.setAttribute("data-collapse", "in");
            e.classList.add("active");
        } else {
            $.getJSON("/comment/" + id, function (data) {
                $.each(data.data.reverse(), function (index, comment) {
                    var mediaLeftElement = $("<div/>", {
                        "class": "media-left"
                    }).append($("<img/>", {
                        "class": "media-object img-rounded myStyle1",
                        "src": comment.user.avatarUrl
                    }));

                    var mediaBodyElement = $("<div/>", {
                        "class": "media-body"
                    }).append($("<h5/>", {
                        "class": "media-heading",
                        "html": comment.user.name
                    })).append($("<div/>", {
                        "html": comment.content
                    })).append($("<div/>", {
                        "class": "menu"
                    }).append($("<span/>", {
                        "class": "pull-right",
                        "html": moment(comment.gmtCreate).format('YYYY-MM-DD')
                    })));

                    var mediaElement = $("<div/>", {
                        "class": "media"
                    }).append(mediaLeftElement).append(mediaBodyElement);

                    var commentElement = $("<div/>", {
                        "class": "col-lg-12 col-md-12 col-sm-12 col-xs-12 comments"
                    }).append(mediaElement);

                    subCommentContainer.prepend(commentElement);
                });
                //展开二级评论
                comments.addClass("in");
                // 标记二级评论展开状态
                e.setAttribute("data-collapse", "in");
                e.classList.add("active");
            });
        }
    }
}