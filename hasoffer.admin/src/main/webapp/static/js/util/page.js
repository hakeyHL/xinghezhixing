function Page(index, size, pCount, tCount, f) {
	this.index = index;
	this.size = size;
	this.pCount = pCount;
	this.tCount = tCount;
	this.pageNos = [];
	this.nextPage = function() {
		if (this.index == this.pCount) {
			return false
		}
		this.index += 1;
		f();
		return true;
	};
	this.prePage = function() {
		if (this.index == 1) {
			return false
		}
		this.index -= 1;
		f();
		return true;
	};
	this.setPage = function(pageModel) {
		this.index = pageModel.pageIndex;
		this.size = pageModel.pageSize;
		this.tCount = pageModel.totalCount;
		this.pCount = pageModel.pageCount;
		this.setPageNos();
	};
	this.setPageNos = function() {
		this.pageNos = [];
		for (var i = 1; i <= this.pCount; i++) {
			this.pageNos.push(i);
		}
	};
	this.changePageIndex = function() {
		f();
	};
};