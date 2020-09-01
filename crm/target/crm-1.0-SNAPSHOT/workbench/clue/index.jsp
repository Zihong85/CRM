<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<!DOCTYPE html>
<html>
<head>
    <base href="<%=basePath%>">
    <meta charset="UTF-8">

    <link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet"/>
    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>

    <link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css"
          rel="stylesheet"/>
    <script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
    <script type="text/javascript"
            src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

    <link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
    <script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
    <script type="text/javascript" src="jquery/bs_pagination/en.js"></script>
    <script type="text/javascript">

        $(function () {
            pageList(1,5);

            $.fn.datetimepicker.dates['zh-CN'] = {
                days: ["星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"],
                daysShort: ["周日", "周一", "周二", "周三", "周四", "周五", "周六", "周日"],
                daysMin: ["日", "一", "二", "三", "四", "五", "六", "日"],
                months: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
                monthsShort: ["1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"],
                today: "今天",
                suffix: [],
                meridiem: ["上午", "下午"]
            };


            $(".time").datetimepicker({
                minView: "month",
                language: 'zh-CN',
                format: 'yyyy-mm-dd',
                autoclose: true,
                todayBtn: true,
                pickerPosition: "top-left"
            });

            $("#searchBtn").click(function () {
                $("#hidden-fullname").val($.trim($("#search-fullname").val()));
                $("#hidden-company").val($.trim($("#search-company").val()));
                $("#hidden-phone").val($.trim($("#search-phone").val()));
                $("#hidden-source").val($.trim($("#search-source").val()));
                $("#hidden-owner").val($.trim($("#search-owner").val()));
                $("#hidden-mphone").val($.trim($("#search-mphone").val()));
                $("#hidden-state").val($.trim($("#search-state").val()));
                pageList(1, 5);
            })

            $("#checkbox").click(function () {
                $("input[name=check]").prop("checked", this.checked);
            })
            $("#clueList").on("click", $("input[name=check]"), function () {
                $("#checkbox").prop("checked", $("input[name=check]").length == $("input[name=check]:checked").length)
            })

            //为创建按钮绑定事件，打开添加操作的模态窗口
            $("#addBtn").click(function () {
                $.ajax({
                    url: "workbench/clue/getUsers.do",
                    type: "get",
                    dataType: "json",
                    success: function (data) {
                        var html = "<option></option>";
                        $.each(data, function (i, n) {
                            html += "<option value='" + n.id + "'>" + n.name + "</option>";
                        })
                        $("#create-owner").html(html);
                        var id = "${user.id}";
                        $("#create-owner").val(id);
                        //处理完所有者下拉框数据后，打开模态窗口
                        $("#createClueModal").modal("show");
                    }
                })
            })
            //为保存按钮绑定事件，执行线索添加操作
            $("#saveBtn").click(function () {
                $.ajax({
                    url: "workbench/clue/save.do",
                    data: {
                        "fullname": $.trim($("#create-fullname").val()),
                        "appellation": $.trim($("#create-appellation").val()),
                        "owner": $.trim($("#create-owner").val()),
                        "company": $.trim($("#create-company").val()),
                        "job": $.trim($("#create-job").val()),
                        "email": $.trim($("#create-email").val()),
                        "phone": $.trim($("#create-phone").val()),
                        "website": $.trim($("#create-website").val()),
                        "mphone": $.trim($("#create-mphone").val()),
                        "state": $.trim($("#create-state").val()),
                        "source": $.trim($("#create-source").val()),
                        "description": $.trim($("#create-description").val()),
                        "contactSummary": $.trim($("#create-contactSummary").val()),
                        "nextContactTime": $.trim($("#create-nextContactTime").val()),
                        "address": $.trim($("#create-address").val())
                    },
                    type: "post",
                    dataType: "json",
                    success: function (data) {
                        if (data.success) {
                            pageList(1, $("#cluePageList").bs_pagination('getOption', 'rowsPerPage'));
                            $("#createClueModal").modal("hide");
                        } else {
                            alert("添加线索失败");
                        }
                    }
                })
            })


            // $("#editBtn").click(function () {
            //     var $a = $("input[name=check]:checked");
            //     if ($a.length == 0) {
            //         alert("请选择要修改的对象");
            //     } else if ($a.length > 1) {
            //         alert("只能选择一条记录进行修改");
            //     } else {
            //         var id = $a.val();
            //         $.ajax({
            //             url: "workbench/clue/edit.do",
            //             data: {"id": id},
            //             type: "post",
            //             dataType: "json",
            //             success: function (data) {
            //                 var html = "<optioin></optioin>";
            //                 $.each(data.list, function (i, n) {
            //                     html += "<option value='" + n.id + "'>" + n.name + "</option>";
            //                 })
            //                 $("#edit-owner").html(html);
            //
            //                 $("#edit-id").val(data.c.id);
            //                 $("#edit-fullname").val(data.c.fullname);
            //                 $("#edit-appellation").val(data.c.appellation);
            //                 $("#edit-owner").val(data.c.owner);
            //                 $("#edit-company").val(data.c.company);
            //                 $("#edit-job").val(data.c.job);
            //                 $("#edit-email").val(data.c.email);
            //                 $("#edit-phone").val(data.c.phone);
            //                 $("#edit-website").val(data.c.website);
            //                 $("#edit-mphone").val(data.c.mphone);
            //                 $("#edit-state").val(data.c.state);
            //                 $("#edit-source").val(data.c.source);
            //                 $("#edit-description").val(data.c.description);
            //                 $("#edit-contactSummary").val(data.c.contactSummary);
            //                 $("#edit-nextContactTime").val(data.c.nextContactTime);
            //                 $("#edit-address").val(data.c.address);
            //
            //                 $("#editClueModal").modal("show");
            //             }
            //         })
            //     }
            // })
            //
            // $("#updateBtn").click(function () {
            //     $.ajax({
            //         url: "workbench/clue/update.do",
            //         data: {
            //             "id": $.trim($("#edit-id").val()),
            //             "fullname": $.trim($("#edit-fullname").val()),
            //             "appellation": $.trim($("#edit-appellation").val()),
            //             "owner": $.trim($("#edit-owner").val()),
            //             "company": $.trim($("#edit-company").val()),
            //             "job": $.trim($("#edit-job").val()),
            //             "email": $.trim($("#edit-email").val()),
            //             "phone": $.trim($("#edit-phone").val()),
            //             "website": $.trim($("#edit-website").val()),
            //             "mphone": $.trim($("#edit-mphone").val()),
            //             "state": $.trim($("#edit-state").val()),
            //             "source": $.trim($("#edit-source").val()),
            //             "description": $.trim($("#edit-description").val()),
            //             "contactSummary": $.trim($("#edit-contactSummary").val()),
            //             "nextContactTime": $.trim($("#edit-nextContactTime").val()),
            //             "address": $.trim($("#edit-address").val())
            //         },
            //         type: "post",
            //         dataType: "json",
            //         success: function (data) {
            //             if (data.success) {
            //                 pageList($("#cluePageList").bs_pagination('getOption', 'currentPage')
            //                     , $("#cluePageList").bs_pagination('getOption', 'rowsPerPage'));
            //                 // 关闭模态窗口
            //                 $("#editClueModal").modal("hide");
            //             } else {
            //                 alert("修改线索失败！")
            //             }
            //         }
            //     })
            // })

            $("#deleteBtn").click(function () {
                var $check = $("input[name=check]:checked");
                if ($check.length == 0) {
                    alert("请选择要删除的记录");
                } else {
                    var param = "";
                    for (var i = 0; i < $check.length; i++) {
                        param += "id=" + $($check[i]).val();
                        if (i < $check.length - 1) {
                            param += "&"
                        }
                    }
                    if (confirm("确定删除所选择的记录吗？")) {
                        $.ajax({
                            url: "workbench/clue/delete.do",
                            data: param,
                            type: "post",
                            dataType: "json",
                            success: function (data) {
                                if (data.success) {
                                    pageList(1, $("#cluePageList").bs_pagination('getOption', 'rowsPerPage'));
                                } else {
                                    alert("删除市场活动失败");
                                }
                            }
                        })
                    }
                }
            })
        });

        function pageList(pageNo, pageSize) {
            $("#checkbox").prop("checked", false);
            $("#search-fullname").val($.trim($("#hidden-fullname").val()));
            $("#search-company").val($.trim($("#hidden-company").val()));
            $("#search-phone").val($.trim($("#hidden-phone").val()));
            $("#search-source").val($.trim($("#hidden-source").val()));
            $("#search-owner").val($.trim($("#hidden-owner").val()));
            $("#search-mphone").val($.trim($("#hidden-mphone").val()));
            $("#search-state").val($.trim($("#hidden-state").val()));
            $.ajax({
                url: "workbench/clue/pageList.do",
                data: {
                    "pageNo": pageNo,
                    "pageSize": pageSize,
                    "fullname":$.trim($("#search-fullname").val()),
                    "company":$.trim($("#search-company").val()),
                    "phone":$.trim($("#search-phone").val()),
                    "source":$.trim($("#search-source").val()),
                    "owner":$.trim($("#search-owner").val()),
                    "mphone":$.trim($("#search-mphone").val()),
                    "state":$.trim($("#search-state").val())
                },
                type: "get",
                dataType: "json",
                success: function (data) {
                    $("#clueList").html("");
                    var html = "";
                    $.each(data.clues, function (i, n) {
                        html += "<tr class=\"clue\">";
                        html += "<td><input type=\"checkbox\" name='check' value='" + n.id + "'/></td>"
                        html += "<td><a style=\"text-decoration: none; cursor: pointer;\" onclick=\"window.location.href='workbench/clue/detail.do?id=" +n.id+ "';\">" + n.fullname + n.appellation + "</a></td >";
                        html += "<td>" + n.company + "</td>";
                        html += "<td>" + n.phone + "</td>";
                        html += "<td>" + n.mphone + "</td>";
                        html += "<td>" + n.source + "</td>";
                        html += "<td>" + n.owner + "</td>";
                        html += "<td>" + n.state + "</td>";
                        html += "</tr>"
                    })
                    $("#clueList").append(html);
                    var totalPages = Math.ceil(data.total / pageSize);

                    $("#cluePageList").bs_pagination({
                        currentPage: pageNo, // 页码
                        rowsPerPage: pageSize, // 每页显示的记录条数
                        maxRowsPerPage: 20, // 每页最多显示的记录条数
                        totalPages: totalPages, // 总页数
                        totalRows: data.total, // 总记录条数

                        visiblePageLinks: 10, // 显示几个卡片

                        showGoToPage: true,
                        showRowsPerPage: true,
                        showRowsInfo: true,
                        showRowsDefaultInfo: true,
                        onChangePage: function (event, data) {
                            pageList(data.currentPage, data.rowsPerPage);
                        }
                    })
                }
            })
        }

    </script>
</head>
<body>

<input type="hidden" id="hidden-fullname">
<input type="hidden" id="hidden-company">
<input type="hidden" id="hidden-phone">
<input type="hidden" id="hidden-source">
<input type="hidden" id="hidden-owner">
<input type="hidden" id="hidden-mphone">
<input type="hidden" id="hidden-state">

<!-- 创建线索的模态窗口 -->
<div class="modal fade" id="createClueModal" role="dialog">
    <div class="modal-dialog" role="document" style="width: 90%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title" id="myModalLabel">创建线索</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form">

                    <div class="form-group">
                        <label for="create-owner" class="col-sm-2 control-label">所有者<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="create-owner">
                            </select>
                        </div>
                        <label for="create-company" class="col-sm-2 control-label">公司<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-company">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="create-appellation" class="col-sm-2 control-label">称呼</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="create-appellation">
                                <option></option>
                                <c:forEach items="${appellation}" var="a">
                                    <option value="${a.value}">${a.text}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <label for="create-fullname" class="col-sm-2 control-label">姓名<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-fullname">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="create-job" class="col-sm-2 control-label">职位</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-job">
                        </div>
                        <label for="create-email" class="col-sm-2 control-label">邮箱</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-email">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="create-phone" class="col-sm-2 control-label">公司座机</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-phone">
                        </div>
                        <label for="create-website" class="col-sm-2 control-label">公司网站</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-website">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="create-mphone" class="col-sm-2 control-label">手机</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-mphone">
                        </div>
                        <label for="create-state" class="col-sm-2 control-label">线索状态</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="create-state">
                                <option></option>
                                <c:forEach items="${clueState}" var="c">
                                    <option value="${c.value}">${c.text}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="create-source" class="col-sm-2 control-label">线索来源</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="create-source">
                                <option></option>
                                <c:forEach items="${source}" var="s">
                                    <option value="${s.value}">${s.text}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>


                    <div class="form-group">
                        <label for="create-description" class="col-sm-2 control-label">线索描述</label>
                        <div class="col-sm-10" style="width: 81%;">
                            <textarea class="form-control" rows="3" id="create-description"></textarea>
                        </div>
                    </div>

                    <div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative;"></div>

                    <div style="position: relative;top: 15px;">
                        <div class="form-group">
                            <label for="create-contactSummary" class="col-sm-2 control-label">联系纪要</label>
                            <div class="col-sm-10" style="width: 81%;">
                                <textarea class="form-control" rows="3" id="create-contactSummary"></textarea>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="create-nextContactTime" class="col-sm-2 control-label">下次联系时间</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control time" id="create-nextContactTime">
                            </div>
                        </div>
                    </div>

                    <div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative; top : 10px;"></div>

                    <div style="position: relative;top: 20px;">
                        <div class="form-group">
                            <label for="create-address" class="col-sm-2 control-label">详细地址</label>
                            <div class="col-sm-10" style="width: 81%;">
                                <textarea class="form-control" rows="1" id="create-address"></textarea>
                            </div>
                        </div>
                    </div>
                </form>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="saveBtn">保存</button>
            </div>
        </div>
    </div>
</div>

<%--<!-- 修改线索的模态窗口 -->--%>
<%--<div class="modal fade" id="editClueModal" role="dialog">--%>
<%--    <div class="modal-dialog" role="document" style="width: 90%;">--%>
<%--        <div class="modal-content">--%>
<%--            <div class="modal-header">--%>
<%--                <button type="button" class="close" data-dismiss="modal">--%>
<%--                    <span aria-hidden="true">×</span>--%>
<%--                </button>--%>
<%--                <h4 class="modal-title">修改线索</h4>--%>
<%--            </div>--%>
<%--            <div class="modal-body">--%>
<%--                <form class="form-horizontal" role="form">--%>

<%--                    <div class="form-group">--%>
<%--                        <label for="edit-owner" class="col-sm-2 control-label">所有者<span--%>
<%--                                style="font-size: 15px; color: red;">*</span></label>--%>
<%--                        <div class="col-sm-10" style="width: 300px;">--%>
<%--                            <select class="form-control" id="edit-owner">--%>
<%--                            </select>--%>
<%--                        </div>--%>
<%--                        <label for="edit-company" class="col-sm-2 control-label">公司<span--%>
<%--                                style="font-size: 15px; color: red;">*</span></label>--%>
<%--                        <div class="col-sm-10" style="width: 300px;">--%>
<%--                            <input type="text" class="form-control" id="edit-company">--%>
<%--                        </div>--%>
<%--                    </div>--%>

<%--                    <div class="form-group">--%>
<%--                        <label for="edit-appellation" class="col-sm-2 control-label">称呼</label>--%>
<%--                        <div class="col-sm-10" style="width: 300px;">--%>
<%--                            <select class="form-control" id="edit-appellation">--%>
<%--                                <option></option>--%>
<%--                                <c:forEach items="${appellation}" var="a">--%>
<%--                                    <option value="${a.value}">${a.text}</option>--%>
<%--                                </c:forEach>--%>
<%--                            </select>--%>
<%--                        </div>--%>
<%--                        <label for="edit-fullname" class="col-sm-2 control-label">姓名<span--%>
<%--                                style="font-size: 15px; color: red;">*</span></label>--%>
<%--                        <div class="col-sm-10" style="width: 300px;">--%>
<%--                            <input type="text" class="form-control" id="edit-fullname">--%>
<%--                        </div>--%>
<%--                    </div>--%>

<%--                    <div class="form-group">--%>
<%--                        <label for="edit-job" class="col-sm-2 control-label">职位</label>--%>
<%--                        <div class="col-sm-10" style="width: 300px;">--%>
<%--                            <input type="text" class="form-control" id="edit-job">--%>
<%--                        </div>--%>
<%--                        <label for="edit-email" class="col-sm-2 control-label">邮箱</label>--%>
<%--                        <div class="col-sm-10" style="width: 300px;">--%>
<%--                            <input type="text" class="form-control" id="edit-email">--%>
<%--                        </div>--%>
<%--                    </div>--%>

<%--                    <div class="form-group">--%>
<%--                        <label for="edit-phone" class="col-sm-2 control-label">公司座机</label>--%>
<%--                        <div class="col-sm-10" style="width: 300px;">--%>
<%--                            <input type="text" class="form-control" id="edit-phone">--%>
<%--                        </div>--%>
<%--                        <label for="edit-website" class="col-sm-2 control-label">公司网站</label>--%>
<%--                        <div class="col-sm-10" style="width: 300px;">--%>
<%--                            <input type="text" class="form-control" id="edit-website">--%>
<%--                        </div>--%>
<%--                    </div>--%>

<%--                    <div class="form-group">--%>
<%--                        <label for="edit-mphone" class="col-sm-2 control-label">手机</label>--%>
<%--                        <div class="col-sm-10" style="width: 300px;">--%>
<%--                            <input type="text" class="form-control" id="edit-mphone">--%>
<%--                        </div>--%>
<%--                        <label for="edit-state" class="col-sm-2 control-label">线索状态</label>--%>
<%--                        <div class="col-sm-10" style="width: 300px;">--%>
<%--                            <select class="form-control" id="edit-state">--%>
<%--                                <option></option>--%>
<%--                                <option></option>--%>
<%--                                <c:forEach items="${clueState}" var="c">--%>
<%--                                    <option value="${c.value}">${c.text}</option>--%>
<%--                                </c:forEach>--%>
<%--                            </select>--%>
<%--                        </div>--%>
<%--                    </div>--%>

<%--                    <div class="form-group">--%>
<%--                        <label for="edit-source" class="col-sm-2 control-label">线索来源</label>--%>
<%--                        <div class="col-sm-10" style="width: 300px;">--%>
<%--                            <select class="form-control" id="edit-source">--%>
<%--                                <option></option>--%>
<%--                                <c:forEach items="${source}" var="s">--%>
<%--                                    <option value="${s.value}">${s.text}</option>--%>
<%--                                </c:forEach>--%>
<%--                            </select>--%>
<%--                        </div>--%>
<%--                    </div>--%>

<%--                    <div class="form-group">--%>
<%--                        <label for="edit-description" class="col-sm-2 control-label">描述</label>--%>
<%--                        <div class="col-sm-10" style="width: 81%;">--%>
<%--                            <textarea class="form-control" rows="3" id="edit-description"></textarea>--%>
<%--                        </div>--%>
<%--                    </div>--%>

<%--                    <div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative;"></div>--%>

<%--                    <div style="position: relative;top: 15px;">--%>
<%--                        <div class="form-group">--%>
<%--                            <label for="edit-contactSummary" class="col-sm-2 control-label">联系纪要</label>--%>
<%--                            <div class="col-sm-10" style="width: 81%;">--%>
<%--                                <textarea class="form-control" rows="3" id="edit-contactSummary"></textarea>--%>
<%--                            </div>--%>
<%--                        </div>--%>
<%--                        <div class="form-group">--%>
<%--                            <label for="edit-nextContactTime" class="col-sm-2 control-label time">下次联系时间</label>--%>
<%--                            <div class="col-sm-10" style="width: 300px;">--%>
<%--                                <input type="text" class="form-control time" id="edit-nextContactTime">--%>
<%--                            </div>--%>
<%--                        </div>--%>
<%--                    </div>--%>

<%--                    <div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative; top : 10px;"></div>--%>

<%--                    <div style="position: relative;top: 20px;">--%>
<%--                        <div class="form-group">--%>
<%--                            <label for="edit-address" class="col-sm-2 control-label">详细地址</label>--%>
<%--                            <div class="col-sm-10" style="width: 81%;">--%>
<%--                                <textarea class="form-control" rows="1" id="edit-address"></textarea>--%>
<%--                            </div>--%>
<%--                        </div>--%>
<%--                    </div>--%>
<%--                    <input type="hidden" id="edit-id">--%>
<%--                </form>--%>

<%--            </div>--%>
<%--            <div class="modal-footer">--%>
<%--                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>--%>
<%--                <button type="button" class="btn btn-primary" id="updateBtn">更新</button>--%>
<%--            </div>--%>
<%--        </div>--%>
<%--    </div>--%>
<%--</div>--%>


<div>
    <div style="position: relative; left: 10px; top: -10px;">
        <div class="page-header">
            <h3>线索列表</h3>
        </div>
    </div>
</div>

<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">

    <div style="width: 100%; position: absolute;top: 5px; left: 10px;">

        <div class="btn-toolbar" role="toolbar" style="height: 80px;">
            <form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">名称</div>
                        <input class="form-control" type="text" id="search-fullname">
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">公司</div>
                        <input class="form-control" type="text" id="search-company">
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">公司座机</div>
                        <input class="form-control" type="text" id="search-phone">
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">线索来源</div>
                        <select class="form-control" id="search-source">
                            <option></option>
                            <c:forEach items="${source}" var="s">
                                <option value="${s.value}">${s.text}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>

                <br>

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">所有者</div>
                        <input class="form-control" type="text" id="search-owner">
                    </div>
                </div>


                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">手机</div>
                        <input class="form-control" type="text" id="search-mphone"/>
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">线索状态</div>
                        <select class="form-control" id="search-state">
                            <option></option>
                            <c:forEach items="${clueState}" var="c">
                                <option value="${c.value}">${c.text}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>

                <button type="button" class="btn btn-default" id="searchBtn">查询</button>

            </form>
        </div>
        <div class="btn-toolbar" role="toolbar"
             style="background-color: #F7F7F7; height: 50px; position: relative;top: 40px;">
            <div class="btn-group" style="position: relative; top: 18%;">
                <button type="button" class="btn btn-primary" id="addBtn">
                    <span class="glyphicon glyphicon-plus"></span> 创建
                </button>
<%--                <button type="button" class="btn btn-default" id="editBtn"><span--%>
<%--                        class="glyphicon glyphicon-pencil"></span> 修改--%>
<%--                </button>--%>
                <button type="button" class="btn btn-danger" id="deleteBtn">
                    <span class="glyphicon glyphicon-minus"></span> 删除
                </button>
            </div>


        </div>
        <div style="position: relative;top: 50px;">
            <table class="table table-hover">
                <thead>
                <tr style="color: #B3B3B3;">
                    <td><input type="checkbox" id="checkbox"/></td>
                    <td>名称</td>
                    <td>公司</td>
                    <td>公司座机</td>
                    <td>手机</td>
                    <td>线索来源</td>
                    <td>所有者</td>
                    <td>线索状态</td>
                </tr>
                </thead>
                <tbody id="clueList">
                </tbody>
            </table>
        </div>

        <div style="height: 50px; position: relative;top: 30px;">
            <div id="cluePageList">

            </div>
        </div>

    </div>

</div>
</body>
</html>