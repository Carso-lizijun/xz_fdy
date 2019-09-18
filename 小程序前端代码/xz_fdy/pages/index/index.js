const app = getApp()

Page({
  data: {
    //用于分页的属性
    totalpage: 1,
    page: 1,
    videoList: [],
    screenWidth: 350,
    serverUrl: "",
    searchContent: ""
  },

  onLoad: function(params) {

    var me = this;
    var screenWidth = wx.getSystemInfoSync().screenWidth;
    me.setData({
      screenWidth: screenWidth,
    });
    if (params == null || params == '' || params == undefined) {
      var isSaveRecord = params.isSaveRecord;
      var searchContent = params.search;

      me.setData({
        searchContent: searchContent
      })
    }

    if (isSaveRecord == null || isSaveRecord == '' || isSaveRecord == undefined) {
      isSaveRecord = 0;
    }

    //获取当前的分页数量 
    var page = me.data.page;
    me.getAllVideoList(page, isSaveRecord);
  },

  getAllVideoList: function(page, isSaveRecord) {
    var me = this;
    var serverUrl = app.serverUrl;
    wx.showLoading({
      title: '请等待，加载中..',
    })
    var searchContent = me.data.searchContent;

    wx.request({
      url: serverUrl + '/video/showAll?page=' + page + '&isSaveRecord=' + isSaveRecord,
      method: 'POST',
      data: {
        videoDesc: searchContent
      },
      success: function(res) {
        wx.hideLoading();
        wx.hideNavigationBarLoading();
        wx.stopPullDownRefresh();

        //判断当前页page是否是第一页，如果是第一页，那么就把videosList清空
        if (page === 1) {
          me.setData({
            videoList: []
          })
        }
        var newVideoList = me.data.videoList;
        var videoList = res.data.data.rows;


        me.setData({
          videoList: newVideoList.concat(videoList),
          page: page,
          totalpage: res.data.data.total,
          serverUrl: serverUrl
        })

      }
    })
  },
  onPullDownRefresh: function() {
    wx.showNavigationBarLoading();
    this.getAllVideoList(1, 0);

  },
  onReachBottom: function() {
    var me = this;
    var currentpage = me.data.page;
    var totalpage = me.data.totalpage;
    //判断当前页和总页数是否相同，相同则无需查询
    if (currentpage === totalpage) {
      wx.showToast({
        title: '已经没有视频了~~',
        icon: "none",
        duration: 2000
      })
      return;
    }
    var page = currentpage + 1;

    me.getAllVideoList(page, 0);
  },
  showVideoInfo: function(e) {
    var me = this;
    var videoList = me.data.videoList;
    var arrindex = e.target.dataset.arrindex;
    var videoInfo = JSON.stringify(videoList[arrindex]);
    wx.redirectTo({
      url: '../videoinfo/videoinfo?videoInfo=' + videoInfo,
    })
  }


})