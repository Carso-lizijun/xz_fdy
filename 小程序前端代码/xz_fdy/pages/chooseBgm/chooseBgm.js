const app = getApp()

Page({
  data: {
    serverUrl: '',
    bgmList: [],
    videoParams: {},
  },
  onLoad: function(params) {
    var me = this;
    var serverUrl = app.serverUrl;
    var userInfo = app.getGlobalUserInfo();

    me.setData({
      videoParams: params
    })
    wx.request({
      url: serverUrl + '/bgm/list',
      method: "POST",
      header: {
        'content-type': 'application/json', // 默认值
        'userId': userInfo.id,
        'userToken': userInfo.userToken
      },
      success: function(res) {
        console.log(res.data);
        wx.hideLoading();
        if (res.data.status == 200) {
          var bgmList = res.data.data;
          me.setData({
            bgmList: bgmList,
            serverUrl: serverUrl
          })
        }
      }
    })

  },
  upload: function(e) {
    var me = this;
    var serverUrl = app.serverUrl;
    var userInfo = app.getGlobalUserInfo();

    var bgmId = e.detail.value.bgmId;
    var desc = e.detail.value.desc;

    var duration = me.data.videoParams.duration;
    var tmpHeight = me.data.videoParams.tmpHeight;
    var tmpWidth = me.data.videoParams.tmpWidth;
    var tmpVideoUrl = me.data.videoParams.tmpVideoUrl;
    var tmpCoverUrl = me.data.videoParams.tmpCoverUrl;
    
    wx.showToast({
      title: '上传中...',
      icon: "none",
      duration: 3000
    });
    wx.uploadFile({
      url: serverUrl + '/video/upload',
      filePath: tmpVideoUrl,
      formData: {
        userId: userInfo.id,
        bgmId: bgmId,
        desc: desc,
        videoSeconds: duration,
        videoHeight: parseInt(tmpHeight),
        videoWidth: parseInt(tmpWidth),
      },
      name: 'file',
      header: {
        'content-type': 'application/json', // 默认值
        'userId': userInfo.id,
        'userToken': userInfo.userToken
      },
      success: function(res) {
        var data = JSON.parse(res.data);
        console.log(data);
        wx.hideLoading();
        if (data.status == 200) {
          wx.showToast({
            title: '上传成功~~',
            duration:2500,
            icon: "success"
          })
          wx.navigateBack({
            delta: 1,
          })
         
        }

      }
    })
  }
})