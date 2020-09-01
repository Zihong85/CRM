<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
            + request.getContextPath() + "/";
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
            pageList(1, 5);
            $("#addBtn").click(function () {
                $.ajax({
                    url: "workbench/activity/getUsers.do",
                    type: "get",
                    dataType: "json",
                    contentType: "application/json",
                    success: function (data) {
                        var html = "<optioin></optioin>";
                        $.each(data, function (i, n) {
                            html += "<option value='" + n.id + "'>" + n.name + "</option>";
                        })
                        $("#create-marketActivityOwner").html(html);
                        $("#create-marketActivityOwner").val("${user.id}");
                        $("#createActivityModal").modal("show");
                    }
                })
            })

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
                format: "yyyy-mm-dd",
                autoclose: true,
                todayBtn: true,
                pickerPosition: "bottom-left"
            });

            $("#saveBtn").click(function () {
                $.ajax({
                    url: "workbench/activity/save.do",
                    data: {
                        "owner": $.trim($("#create-marketActivityOwner").val()),
                        "name": $.trim($("#create-marketActivityName").val()),
                        "startDate": $.trim($("#create-startDate").val()),
                        "endDate": $.trim($("#create-endDate").val()),
                        "cost": $.trim($("#create-cost").val()),
                        "description": $.trim($("#create-describe").val())
                    },
                    type: "post",
                    dataType: "json",
                    success: function (data) {
                        if (data.success) {
                            pageList(1, $("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
                            $("#activityAddForm")[0].reset();
                            // 关闭模态窗口
                            $("#createActivityModal").modal("hide");
                        } else {
                            alert("添加市场活动失败！")
                        }
                    }
                })
            })

            $("#searchBtn").click(function () {
                $("#hidden-name").val($.trim($("#search-name").val()));
                $("#hidden-owner").val($.trim($("#search-owner").val()));
                $("#hidden-startDate").val($.trim($("#search-startDate").val()));
                $("#hidden-endDate").val($.trim($("#search-endDate").val()));
                pageList(1, 5);
            })

            $("#checkbox").click(function () {
                $("input[name=check]").prop("checked", this.checked);
            })
            $("#activitiesList").on("click", $("input[name=check]"), function () {
                $("#checkbox").prop("checked", $("input[name=check]").length == $("input[name=check]:checked").length)
            })


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
                            url: "workbench/activity/delete.do",
                            data: param,
                            type: "post",
                            dataType: "json",
                            success: function (data) {
                                if (data.success) {
                                    pageList(1, $("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
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
            $("#search-name").val($.trim($("#hidden-name").val()));
            $("#search-owner").val($.trim($("#hidden-owner").val()));
            $("#search-startDate").val($.trim($("#hidden-startDate").val()));
            $("#search-endDate").val($.trim($("#hidden-endDate").val()));
            $.ajax({
                url: "workbench/activity/pageList.do",
                data: {
                    "pageNo": pageNo,
                    "pageSize": pageSize,
                    "owner": $.trim($("#search-owner").val()),
                    "name": $.trim($("#search-name").val()),
                    "startDate": $.trim($("#search-startDate").val()),
                    "endDate": $.trim($("#search-endDate").val())
                },
                type: "get",
                dataType: "json",
                success: function (data) {
                    $("#activitiesList").html("");
                    var html = "";
                    $.each(data.activities, function (i, n) {
                        html += "<tr class=\"active\">";
                        html += "<td><input type=\"checkbox\" name='check' value='" + n.id + "'/></td>"
                        html += "<td><a style=\"text-decoration: none; cursor: pointer;\" onclick=\"window.location.href='workbench/activity/detail.do?id=" + n.id + "';\">" + n.name + "</a></td >";
                        html += "<td>" + n.owner + "</td>";
                        html += "<td>" + n.startDate + "</td>";
                        html += "<td>" + n.endDate + "</td>";
                        html += "</tr>"
                    })
                    $("#activitiesList").append(html);
                    var totalPages = Math.ceil(data.total / pageSize);

                    $("#activityPage").bs_pagination({
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

<input type="hidden" id="hidden-name">
<input type="hidden" id="hidden-owner">
<input type="hidden" id="hidden-startDate">
<input type="hidden" id="hidden-endDate">

<!-- 修改市场活动的模态窗口 -->
<%--<div class="modal fade" id="editActivityModal" role="dialog">--%>
<%--    <div class="modal-dialog" role="document" style="width: 85%;">--%>
<%--        <div class="modal-content">--%>
<%--            <div class="modal-header">--%>
<%--                <button type="button" class="close" data-dismiss="modal">--%>
<%--                    <span aria-hidden="true">×</span>--%>
<%--                </button>--%>
<%--                <h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>--%>
<%--            </div>--%>
<%--            <div class="modal-body">--%>

<%--                <form class="form-horizontal" role="form">--%>

<%--                    <div class="form-group">--%>
<%--                        <label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span--%>
<%--                                style="font-size: 15px; color: red;">*</span></label>--%>
<%--                        <div class="col-sm-10" style="width: 300px;">--%>
<%--                            <select class="form-control" id="edit-marketActivityOwner">--%>
<%--                            </select>--%>
<%--                        </div>--%>
<%--                        <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span--%>
<%--                                style="font-size: 15px; color: red;">*</span></label>--%>
<%--                        <div class="col-sm-10" style="width: 300px;">--%>
<%--                            <input type="text" class="form-control" id="edit-marketActivityName" value="发传单">--%>
<%--                        </div>--%>
<%--                    </div>--%>

<%--                    <div class="form-group">--%>
<%--                        <label for="edit-startDate" class="col-sm-2 control-label">开始日期</label>--%>
<%--                        <div class="col-sm-10" style="width: 300px;">--%>
<%--                            <input type="text" class="form-control time" id="edit-startDate">--%>
<%--                        </div>--%>
<%--                        <label for="edit-endDate" class="col-sm-2 control-label">结束日期</label>--%>
<%--                        <div class="col-sm-10" style="width: 300px;">--%>
<%--                            <input type="text" class="form-control time" id="edit-endDate">--%>
<%--                        </div>--%>
<%--                    </div>--%>

<%--                    <div class="form-group">--%>
<%--                        <label for="edit-cost" class="col-sm-2 control-label">成本</label>--%>
<%--                        <div class="col-sm-10" style="width: 300px;">--%>
<%--                            <input type="text" class="form-control" id="edit-cost">--%>
<%--                        </div>--%>
<%--                    </div>--%>

<%--                    <div class="form-group">--%>
<%--                        <label for="edit-describe" class="col-sm-2 control-label">描述</label>--%>
<%--                        <div class="col-sm-10" style="width: 81%;">--%>
<%--                            <textarea class="form-control" rows="3" id="edit-describe">市场活动Marketing，是指品牌主办或参与的展览会议与公关市场活动，包括自行主办的各类研讨会、客户交流会、演示会、新产品发布会、体验会、答谢会、年会和出席参加并布展或演讲的展览会、研讨会、行业交流会、颁奖典礼等</textarea>--%>
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


<!-- 创建市场活动的模态窗口 -->
<div class="modal fade" id="createActivityModal" role="dialog">
    <div class="modal-dialog" role="document" style="width: 85%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
            </div>
            <div class="modal-body">

                <form id="activityAddForm" class="form-horizontal" role="form">

                    <div class="form-group">
                        <label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="create-marketActivityOwner">
                            </select>
                        </div>
                        <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-marketActivityName">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="create-startDate" class="col-sm-2 control-label">开始日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control time" id="create-startDate">
                        </div>
                        <label for="create-endDate" class="col-sm-2 control-label">结束日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control time" id="create-endDate">
                        </div>
                    </div>
                    <div class="form-group">

                        <label for="create-cost" class="col-sm-2 control-label">成本</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-cost">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="create-describe" class="col-sm-2 control-label">描述</label>
                        <div class="col-sm-10" style="width: 81%;">
                            <textarea class="form-control" rows="3" id="create-describe"></textarea>
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

<div>
    <div style="position: relative; left: 10px; top: -10px;">
        <div class="page-header">
            <h3>市场活动列表</h3>
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
                        <input class="form-control" type="text" id="search-name">
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">所有者</div>
                        <input class="form-control" type="text" id="search-owner">
                    </div>
                </div>


                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">开始日期</div>
                        <input class="form-control time" type="text" id="search-startDate"/>
                    </div>
                </div>
                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">结束日期</div>
                        <input class="form-control time" type="text" id="search-endDate">
                    </div>
                </div>

                <button type="button" id="searchBtn" class="btn btn-default">查询</button>

            </form>
        </div>
        <div class="btn-toolbar" role="toolbar"
             style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
            <div class="btn-group" style="position: relative; top: 18%;">

                <button type="button" class="btn btn-primary" id="addBtn">
                    <span class="glyphicon glyphicon-plus"></span> 创建
                </button>
                <%--                <button type="button" class="btn btn-default" id="editBtn"><span--%>
                <%--                        class="glyphicon glyphicon-pencil"></span> 修改--%>
                <%--                </button>--%>
                <button type="button" class="btn btn-danger" id="deleteBtn"><span
                        class="glyphicon glyphicon-minus"></span> 删除
                </button>

            </div>

        </div>
        <div style="position: relative;top: 10px;">
            <table class="table table-hover">
                <thead>
                <tr style="color: #B3B3B3;">
                    <td><input type="checkbox" id="checkbox"/></td>
                    <td>名称</td>
                    <td>所有者</td>
                    <td>开始日期</td>
                    <td>结束日期</td>
                </tr>
                </thead>
                <tbody id="activitiesList">
                </tbody>
            </table>
        </div>

        <div style="height: 50px; position: relative;top: 30px;">
            <div id="activityPage">

            </div>
        </div>

    </div>

</div>
</body>
</html>