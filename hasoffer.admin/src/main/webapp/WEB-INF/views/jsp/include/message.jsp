<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div id="messageDiv" class="centerDiv messageDiv">
	<div id="messageContent">
		hello , allbuy!
	</div>
</div>

<div id="messageDialog" title="Message">
	<p id="warnMessageContent">

	</p>
</div>

<script>
	$("#messageDialog").dialog({
		autoOpen : false,
		modal : true,
		width : 600,
		buttons : {
			Ok : function() {
				$(this).dialog("close");
			}
		}
	});

	var abMessage = {
		show : function(msg) {
			$("#messageContent").html(msg);
			$("#messageDiv").fadeIn();
			$("#messageDiv").fadeOut(2000);
		},
		warn : function(msg) {
			$("#warnMessageContent").html(msg);
			$('#messageDialog').dialog('open');
			return false;
		}
	}
</script>

<style>
	#messageContent {
		margin-top: 15px;
	}

	.messageDiv {
		display: none;
		position: fixed;
		z-index: 1001;
		margin-left: -150px;
		margin-top: -100px;
		width: 300px;
		height: 60px;
		color: red;
		font-size: 20px;
		background-color: wheat;
		border: 1px solid yellowgreen;
	}
</style>
