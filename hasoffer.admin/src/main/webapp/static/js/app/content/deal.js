/**
 * Created by Administrator on 2015/10/27.
 */
var richDescEditor = null;
var editVo;
var brandList = null;
var models = {};
var index = 1;  // 用于放置出现重复的id
var productSelPattern = '<div id="brandModelIndex" class="form-group">' +
				'品牌 <select id="brandIndex" class="brand-sel" data-row="modelIndex"></select>' +
				'型号 <select id="modelIndex" class="model-sel"></select>' +
				'&nbsp;&nbsp;<a href="javascript:void(0)" class="remove-product" data-row="brandModelIndex"><span class="glyphicon glyphicon-remove-circle"></span></a>' +
				'</div>';
var selPattern = '<option value="oValue">oValue</option>';

function editDeal(id)
{
	var dateTimeStamp = new Date();
	$.get('/content/get?id='+id+'&timestamp='+dateTimeStamp.getTime(), function(data){
		row = data;

		$("#sourceUrl").val(row.sourceUrl);
		$("#targetUrl").val(row.targetUrl);

		if (row.expiredTime > 0)
		{
			var date = new Date();
			date.setTime(row.expiredTime);
			$("#expiredTime").val(date.format('yyyy/MM/dd'));
		}

		$("#couponCode").val(row.couponCode);
		$("#coverUrl").val(row.coverUrl);
		$("#title").val(row.title);

		if (brandList == null)
		{
			brandList = row.brandList;
			brandList.unshift('');
		}

		mergeModels(row.models);

		//$("#richDescription").val(row.richDescription);
		if (richDescEditor == null)
		{
			richDescEditor = CKEDITOR.replace('richDescription', {
				language : 'zh-cn',
				allowedContent : true,
				height : 500
			});
		}

		if (row.enabled)
		{
			$("input[name='enableDeal']").prop("checked",'true');
		}
		else
		{
			$("input[name='enableDeal']").removeAttr("checked");
		}

		richDescEditor.setData(row.richDescription);
		editVo = row;
		index = 1;
		$('#productSelArea').empty();
		if (typeof(editVo.brandInfos) != 'undefined' && editVo.brandInfos != null)
		{
			for (var i = 0; i < editVo.brandInfos.length; i++)
			{
				addBrandSelector();
				$('#brand'+index).val(editVo.brandInfos[i][0]);

				var modelOPtions = editVo.models[editVo.brandInfos[i][0]];

				reloadModelSelector($('#model'+index), modelOPtions);

				$('#model'+index).val(editVo.brandInfos[i][1]);

				index++;
			}
		}

		$("#editDealModal").modal("show");
	});
};

function mergeModels(fromModels)
{
	if (typeof (fromModels) == 'undefined' || fromModels == null)
	{
		return;
	}


	for (var attr in fromModels)
	{
		models[attr] = fromModels[attr];
		models[attr].unshift('');
	}
}

function saveDeal()
{
	var content ={
		id : editVo.id,
		sourceUrl : $("#sourceUrl").val(),
		targetUrl : $("#targetUrl").val(),
		expiredTime : '',
		couponCode : $("#couponCode").val(),
		coverUrl : $("#coverUrl").val(),
		title : $("#title").val(),
		richDescription : '',
		brandInfos : [],
		enabled : $("input[name='enableDeal']").is(':checked')
	};

	var localeTime = $("#expiredTime").val();
	if (localeTime != null && localeTime != '')
	{
		content.expiredTime = new Date(localeTime).valueOf();
	}

	content.richDescription = richDescEditor.getData();

	var loopRes = true;
	$("select[id^='brand']").each(function(){
		var brandV = $(this).val();
		if (brandV == null || brandV == '')
		{
			alert('商品的品牌不能为空');
			loopRes = false;
			return false;
		}

		var rowV = $(this).data('row');
		var modelV = $('#'+rowV).val();
		if (modelV == null || modelV == '')
		{
			alert('商品的型号不能为空');
			loopRes = false;
			return false;
		}

		content.brandInfos.push([brandV, modelV]);
	});

	if (loopRes == false)
	{
		return;
	}

	var postObj = {
		dealJson : JSON.stringify(content)
	}

	$.post('/content/save', postObj, function(data)
	{
		if (data == 'success')
		{
			$("#editDealModal").modal("hide");
			editVo = null;
		}
	});
};

var addProduct = function() {
	addBrandSelector();
	index++;
};

function addBrandSelector()
{
	var tmp = productSelPattern.replace(/Index/g, index);
	$('#productSelArea').append(tmp);
	for (var j = 0; j < brandList.length; j++)
	{
		var tmpOpStr = selPattern.replace(/oValue/g, brandList[j]);
		$('#brand'+index).append(tmpOpStr);
	}

	// when add new selector, no brand selected yet, so don't need to set model seletor
	$('#brand'+index).change(function(){
		var id = this.id;
		var indexNum = id.substr(5);
		var selBrand = this.value;
		if (typeof (models[selBrand]) == 'undefined' || models[selBrand] == null)
		{
			$.get('/content/getModels?brandName='+selBrand, function(data){
				if (data != null)
				{
					data.unshift('');
					models[selBrand] = data;
				}
				else
				{
					alert("Get brand models failed, please contact the developers to resolve this issue!");
					return;
				}

				reloadModelSelector($('#model'+indexNum), models[selBrand]);
			});
		}
		else
		{
			reloadModelSelector($('#model'+indexNum), models[selBrand]);
		}
	});

	$('.remove-product').click(function(){
		var row=$(this).data("row");
		$('#productSelArea').children('#'+row).remove();
	})
}

/*
 modelSel is the jquery object of the selector
 modelArray is a js array
 */
function reloadModelSelector(modelSel, modelArray){
	modelSel.empty();
	if (typeof (modelArray) == 'undefined' || modelArray == null)
	{
		return;
	}

	for (var i = 0; i < modelArray.length; i++)
	{
		var tmpOpStr = selPattern.replace(/oValue/g, modelArray[i]);
		modelSel.append(tmpOpStr);
	}
}

$(document).ready(function(){
	$(".d_edit").click(function(){
		var row=$(this).data("row");
		editDeal(row);
	});

	$('#saveD').click(function(){
		saveDeal();
	});

	$('#addP').click(function(){
		addProduct();
	});

	$('#expiredTime')
			.datetimepicker({
				language : 'zh-CN',
				autoclose : true,
				todayBtn : true,
				format : 'yyyy/mm/dd hh:ii'
			});
	//$('#dealForm').submit()
});