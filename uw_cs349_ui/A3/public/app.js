/*
 *  Starter code for University of Waterloo CS349 Fall 2016.
 *  
 *  bwbecker 20161113
 *  
 *  Some code adapted from https://github.com/possan/playlistcreator-example
 */
"use strict";

// An anonymous function that is executed passing "window" to the
// parameter "exports".  That is, it exports startApp to the window
// environment.
(function(exports) {

	var client_id = '00f34337c1354496b2bcce580c0e42da';		// Fill in with your value from Spotify
	var redirect_uri = 'http://localhost:3000/index.html';
	var g_access_token = '';


    var Model = function() {

    	this.labels = ["one","two","three"];

        this.addLabel = function(label) {
            this.labels.push(label);
            this.notify(label);
            $.post("http://localhost:3000/demo", {"tag": label }, null , "json");
        }

        this.getLabel = function() {
            return this.labels;
        }

        this.deleteItem = function(idx) {
            this.labels.splice(idx, 1);
            this.notify();
        }
    }

    // Add observer functionality to ListModel objects
    _.assignIn(Model.prototype, {
        // list of observers
        observers: [],
        // Add an observer to the list
        addObserver: function(observer) {
            this.observers.push(observer);
            observer(this, null);
        },

        // Notify all the observers on the list
        notify: function(args) {
            _.forEach(this.observers, function(obs) {
                obs(this, args);
            });
        }
    });

    var LabelView = function(model, div) {
        var that = this;
        this.updateView = function(obs, args) {
            var labels = model.getLabel();

            // Display each pair
            var myDiv = $(div);
            myDiv.empty();
            _.forEach(labels, function(label, idx){
                // Get the template.  It isn't parsed, so can't be 
                // manipulated until after it's been added to the DOM.
                var t = $("template#list_item");
                // turn the html from the template into a DOM element
                var elem = $(t.html());

                elem.find(".key").html(label);
                elem.find(".btn").click(that.makeDeleteItemController(idx));
                myDiv.append(elem);
            });
            that.appendInputRow();
        };

        this.makeDeleteItemController = function(idx) {
            return function() {
                model.deleteItem(idx);
            }
        };

        // Append a blank input row to the div
        this.appendInputRow = function() {
            var template = $("template#list_input").html();
            $(div).append(template);
            $("#input_row .key").focus();
           
            // What to do when the add button is clicked.
            // That is, a controller.

            $("#addItemBtn").click(function() {
                var key = $("#input_row .key").val();
                model.addLabel(key);
                $("#input_row .key").focus();
            });
        };

        model.addObserver(this.updateView);
    }

	/*
	 * Get the playlists of the logged-in user.
	 */
	function getPlaylists(callback) {
		console.log('getPlaylists');
		var url = 'https://api.spotify.com/v1/me/playlists';
		$.ajax(url, {
			dataType: 'json',
			headers: {
				'Authorization': 'Bearer ' + g_access_token
			},
			success: function(r) {
				console.log('got playlist response', r);
				callback(r.items);
			},
			error: function(r) {
				callback(null);
			}
		});
	}

	function getTracks(callback,uid,pid){
		console.log('getTracks');
		var url = 'https://api.spotify.com/v1/users/'+ uid +'/playlists/'+pid+'/tracks';
		$.ajax(url,{
			dataType: 'json',
			headers: {
				'Authorization': 'Bearer ' + g_access_token
			},
			success: function(r) {
				console.log('got tracks response', r);
				callback(r.items);
			},
			error: function(r) {
				callback(null);
			}
		});
	}
	/*
	 * Redirect to Spotify to login.  Spotify will show a login page, if
	 * the user hasn't already authorized this app (identified by client_id).
	 * 
	 */

	var Click = function(item, value) {
		alert("click");
		 $.post("http://localhost:3000/demo", {"tid": item.tid,"rating":value }, null , "json");

	}

	var TrackView = function(){
		$.get("http://localhost:3000/demo", function(data){
			console.log("data:",data);
			for(var item in data){
				if(data[item].pid!=undefined){
					$('#playlists').append('<li id='+data[item].pid+'>' + data[item].playlist + '</li>');
				}
				else if(data[item].track!=undefined){
					$('#'+data[item].inpid).append('<ol id='+data[item].tid+'>' + data[item].track + '</ol>');
					$('#'+data[item].inpid).append('<form action="" method="get">'
					+'<label><input name='+data[item].inpid+' type="radio" value="1" />1 </label>'
					+'<label><input name='+data[item].inpid+' type="radio" value="2" />2 </label>'							
					+'<label><input name='+data[item].inpid+' type="radio" value="3" />3 </label>'
					+'<label><input name='+data[item].inpid+' type="radio" value="4" />4 </label>'
					+'<label><input name='+data[item].inpid+' type="radio" value="5" />5 </label>'
					+'</form>');
					$('#'+data[item].inpid).append('<form action="" method="get">'
					+'<label><input name='+data[item].inpid+' type="checkbox" value="one" />one</label>'
					+'<label><input name='+data[item].inpid+' type="checkbox" value="two" />two</label>'
					+'<label><input name='+data[item].inpid+' type="checkbox" value="three" />three</label>'
					+'</form>'
					+'<ol id='+data[item].inpid+'>----------------------</ol>');


				}
			}

		});




	}

	var doLogin = function(callback) {
		var url = 'https://accounts.spotify.com/authorize?client_id=' + client_id +
			'&response_type=token' +
			'&scope=playlist-read-private' +
			'&redirect_uri=' + encodeURIComponent(redirect_uri);

		console.log("doLogin url = " + url);
		window.location = url;
	}

	function search(){
		$('#loggedin').hide();
		$('#search').show();
	}

	/*
	 * What to do once the user is logged in.
	 */
	function loggedIn() {
		$('#login').hide();
		$('#loggedin').show();
		$('#search').hide();
		var datas;

		//$.post("http://localhost:3000/demo",{""},null,"json");
		$.get("http://localhost:3000/demo", function(datas){

		getPlaylists(function(items) {
			console.log('items = ', items);
			items.forEach(function(item){
			//	$('#playlists').append('<li id='+item.id+'>' + item.name + '</li>');
				for(var data in datas){
				//	alert(data);
					if(datas[data].pid==item.id){
						break;
					}else if(data==datas.length-1){
						$.post("http://localhost:3000/demo",{"playlist":item.name,"pid":item.id},null,"json");
					}
				}

				getTracks(function(titems){
					console.log('items = ',titems);
					titems.forEach(function(titem){
					//	$('#'+item.id).append('<ol id='+titem.track.id+'>' + titem.track.name + '</ol>');
						for(var data in datas){
							if(datas[data].tid==titem.track.id){
								break;
							}else if(data==datas.length-1){
								$.post("http://localhost:3000/demo", {"track": titem.track.name,"tid": titem.track.id,"inpid":item.id}, null , "json");
							}
						}
						
					});
				},item.owner.id,item.id);

			});
		});

		$('#tosearch').click(function(){
			search();
		});

				});	
		// Post data to a server-side database.  See 
		// https://github.com/typicode/json-server
		//var now = new Date();
		//$.post("http://localhost:3000/demo", {"msg": "accessed at " + now.toISOString()}, null, "json");
	}

	/*
	 * Export startApp to the window so it can be called from the HTML's
	 * onLoad event.
	 */
	exports.startApp = function() {
		console.log('start app.');

		console.log('location = ' + location);

		var model = new Model();
		var labelView = new LabelView(model,"#inputDiv")
		var trackView = new TrackView();

		// Parse the URL to get access token, if there is one.
		var hash = location.hash.replace(/#/g, '');
		var all = hash.split('&');
		var args = {};
		all.forEach(function(keyvalue) {
			var idx = keyvalue.indexOf('=');
			var key = keyvalue.substring(0, idx);
			var val = keyvalue.substring(idx + 1);
			args[key] = val;
		});
		console.log('args', args);

		if (typeof(args['access_token']) == 'undefined') {
			$('#start').click(function() {
				doLogin(function() {});
			});
			$('#login').show();
			$('#loggedin').hide();
			$('#search').hide();
		} else {
			g_access_token = args['access_token'];
			loggedIn();
		}
	}

})(window);
