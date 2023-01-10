//Style Sheet Switcher version 1.0 Nov 9th, 2005
//Author: Dynamic Drive: http://www.dynamicdrive.com
//Usage terms: http://www.dynamicdrive.com/notice.htm

// Release 9.0.1 T300017537 starts
if(!String.prototype.trim) {
  String.prototype.trim = function () {
    return this.replace(/^\s+|\s+$/g,'');
  };
}
// Release 9.0.1 T300017537 ends
var isChange = false; 
var keyWord;

function getCookie(Name) { 
var re=new RegExp(Name+"=[^;]+", "i"); //construct RE to search for target name/value pair
if (document.cookie.match(re)) //if cookie found
return document.cookie.match(re)[0].split("=")[1] //return its value
return null
}

function setCookie(name, value, days) {
var expireDate = new Date()
//set "expstring" to either future or past date, to set or delete cookie, respectively
var expstring=(typeof days!="undefined")? expireDate.setDate(expireDate.getDate()+parseInt(days)) : expireDate.setDate(expireDate.getDate()-5)
document.cookie = name+"="+value+"; expires="+expireDate.toGMTString()+"; path=/";
}

function deleteCookie(name){
setCookie(name, "moot")
}

function setStylesheet(title) {
  var i, cacheobj
  for(i=0; (cacheobj=parent.manual_frame.document.getElementsByTagName("link")[i]); i++) {
    if(cacheobj.getAttribute("rel").indexOf("style") != -1 && cacheobj.getAttribute("title")) {
      cacheobj.disabled = true;
      if(cacheobj.getAttribute("title") == title) {
        cacheobj.disabled = false; //enable chosen style sheet
      }
    }
  }
}

function indicateSelected(element){ //Optional function that shows which style sheet is currently selected within group of radio buttons or select menu 
var i
if (selectedtitle!=null && (element.type==undefined || element.type=="select-one")){ //if element is a radio button or select menu
var element=(element.type=="select-one") ? element.options : element
for (i=0; i<element.length; i++){
if (element[i].value==selectedtitle){ //if match found between form element value and cookie value
if (element[i].tagName=="OPTION") //if this is a select menu
element[i].selected=true
else //else if it's a radio button
element[i].checked=true
break;
}
}
}
}

var selectedtitle=getCookie("mysheet")
if (document.getElementById && selectedtitle!=null) //load user chosen style sheet if there is one stored
setStylesheet(selectedtitle)

// BEGIN CUSTOM JS
// Custom js added by JChiu for SI Viewer

    var TRange=null;
    var findState = 'hidden';
    
    // Logic to load CSS files
    function hideDiv(pass) {
        var divs = document.getElementsByTagName('div');
        for(i=0;i<divs.length;i++){
            if(divs[i].id.match(pass)){//if they are 'see' divs
                if (document.getElementById) // DOM3 = IE5, NS6
                    divs[i].style.visibility="hidden";// show/hide
                else
                    if (document.layers) // Netscape 4
                        document.layers[divs[i]].display = 'hidden';
                    else // IE 4
                        document.all.hideshow.divs[i].visibility = 'hidden';
            }
        }
    }
    
    function showDiv(pass) {
        var divs = document.getElementsByTagName('div');
        for(i=0;i<divs.length;i++){
            if(divs[i].id.match(pass)){
                if (document.getElementById)
                    divs[i].style.visibility="visible";
                else
                    if (document.layers) // Netscape 4
                        document.layers[divs[i]].display = 'visible';
                    else // IE 4
                        document.all.hideshow.divs[i].visibility = 'visible';
            }
        }
    }     
function hideDivDisplay(pass) {
        var divs = document.getElementsByTagName('div');
        for(i=0;i<divs.length;i++){
            if(divs[i].id.match(pass)){//if they are 'see' divs
                if (document.getElementById){ // DOM3 = IE5, NS6
                     divs[i].style.display = 'none';// show/hide
              }
            }
        }
    }
    
    function showDivDisplay(pass) {
        var divs = document.getElementsByTagName('div');
        for(i=0;i<divs.length;i++){
            if(divs[i].id.match(pass)){
                if (document.getElementById){
                    divs[i].style.display="block";
                     }
              }
       } 
}
    
    // Release 9.0.1 T300017537 starts
    function showTOC() {
        //document.getElementById('showHideLink').innerHTML = document.getElementById('hideTOCLink').innerHTML;
    	document.getElementById("min").style.display = "none";
		document.getElementById("hideSlash").style.display = "inline";
		document.getElementById("close").style.display = "inline";
		document.getElementById("lastMax").style.display = "none";
		document.getElementById("max").style.display = "inline";
		document.getElementById("lastMaxSlash").style.display = "none";
		document.getElementById("maxSlash").style.display = "none";
		document.getElementById("lastMaxSlashDisabled").style.display = "none";
		document.getElementById("minDisabledTreeId").style.display = "none";
		document.getElementById("lastMaxDisabledTreeId").style.display = "none";
        parent.document.getElementById("subFrameSet").cols = "30%,70%";
        //Release 9.2.1 T300018170 - Start
        parent.control_frame.document.getElementById("treeView").value = "default";
        //Release 9.2.1 T300018170 - End
        
    }

    function hideTOC() {
    	document.getElementById("lastMaxSlashDisabled").style.display = "none";
		document.getElementById("minDisabledTreeId").style.display = "none";
		document.getElementById("lastMaxDisabledTreeId").style.display = "none";
		document.getElementById("min").style.display = "inline";
		document.getElementById("lastMax").style.display = "inline";
		document.getElementById("lastMaxSlash").style.display = "inline";
		document.getElementById("close").style.display = "none";
		document.getElementById("max").style.display = "none";
		document.getElementById("hideSlash").style.display = "none";
		document.getElementById("maxSlash").style.display = "none";
        parent.document.getElementById("subFrameSet").cols = "1,*";
        //Release 9.2.1 T300018170 - Start
        parent.control_frame.document.getElementById("treeView").value = "hide";
        //Release 9.2.1 T300018170 - End
    }
    
    function maxTOC(){
    	document.getElementById("min").style.display = "inline";
		document.getElementById("max").style.display = "none";
		document.getElementById("close").style.display = "inline";
		document.getElementById("hideSlash").style.display = "none";
		document.getElementById("lastMax").style.display = "none";
		document.getElementById("max").style.display = "none";
		document.getElementById("lastMaxSlash").style.display = "none";
		document.getElementById("maxSlash").style.display = "inline";
		document.getElementById("lastMaxSlashDisabled").style.display = "none";
		document.getElementById("minDisabledTreeId").style.display = "none";
		document.getElementById("lastMaxDisabledTreeId").style.display = "none";
		parent.document.getElementById("subFrameSet").cols = "40%,60%";
		//Release 9.2.1 T300018170 - Start
		parent.control_frame.document.getElementById("treeView").value = "max";
		//Release 9.2.1 T300018170 - End
    }
// Release 9.0.1 T300017537 ends
    function print_manual() {
        parent.manual_frame.focus();
        parent.manual_frame.print();
    }

    // ---- START OF Breadcrumb/history functions -----//
    // Start of custom history functions to keep track of back and forward movements
    var currentBCItem; // holds the current breadcrumb position
    // Breadcrumb node structure (linked list item)
    function BCItem() {
        this.value = null;
        this.next = null;
        this.prev = null;
    }
    // Add new node to linked list
    function AddBCItem(v,filterChange, isEmptyBCItem) {
    	//alert("Addbc "+filterChange);
       if(v == null || v == "" || v.indexOf("/null") != -1) return;
       
       if(filterChange == "changed" || isEmptyBCItem == true){
        	currentBCItem = null;
        }
       
       /*if(hrefData != null){
    	   currentBCItem = new BCItem();
           currentBCItem.value = hrefData;
       }*/
        if (currentBCItem == null) {
            currentBCItem = new BCItem();
            currentBCItem.value = v;
        }
        // if there are forward items that should be cleared, then traverse the list and clear them
        if (currentBCItem.value != v) {
        	var temp = currentBCItem.next;
            if (temp != null) {
                while(temp != null && temp.next != null) {
                    temp = temp.next;
                }
                while(temp != currentBCItem) {
                    var temp2 = temp.prev;
                    temp = null;
                    temp = temp2;
                }
            }
            // Add new item to the list starting at current node
        //alert("new1: " + v);
            temp = new BCItem();
            temp.value = v;
            temp.prev = currentBCItem;
            currentBCItem.next = temp;
            currentBCItem = temp;
           // Release 9.0.1 T300017537 starts
            
            document.getElementById("si_back").style.display = "inline";
            document.getElementById("si_back").src = "../../../ewdappu/images/back.jpg";
            document.getElementById("si_forw").style.display = "none";
	    

	    // Release 9.0.1 T300017537 ends
            if(document.getElementById("findInput")!= null && document.getElementById("findInput")!="undefined"){
            	bookmark = new Array();
            	finds = 0;
            	//document.getElementById('findInput').style.width = "20%";
            	document.getElementById('displaySearchResults').className = "hide";
            } 
        }
    }
    
    function goBack() {
    parent.navigation_frame.document.getElementById("isBack").value = "back";
        if (currentBCItem != null && currentBCItem.prev != null) {
        var link="";        
        var tempItem="";        
           if(currentBCItem.value.indexOf("linkId")>0)
            {    
                      
              if(currentBCItem.value.indexOf("hashId")>0)
               {
               link=currentBCItem.value.substring(currentBCItem.value.indexOf("linkId")+7,currentBCItem.value.indexOf("hashId")-1);
               
               }
               else
               {
               link=currentBCItem.value.substring(currentBCItem.value.indexOf("linkId")+7);
               }          
            }
            else
            {  
                  
            if(currentBCItem.prev.value.indexOf("hashId")>0)
            {
            tempItem=currentBCItem.prev;
            link=tempItem.value.substring(tempItem.value.indexOf("hashId")+7);
           
            }            
            } 
            currentBCItem = currentBCItem.prev;
            //parent.frames['manual_frame'].history.back();            
            parent.manual_frame.location.href = currentBCItem.value+'#'+link;
	    // Release 9.0.1 T300017537 starts
            document.getElementById("si_forw").style.display = "inline";
            document.getElementById("si_forw").src = "../../../ewdappu/images/forward.jpg";
            //showDiv("forwardLinkDiv");
	    // Release 9.0.1 T300017537 ends
        }
        if (currentBCItem != null) {
            if (currentBCItem.prev != null) {
	    // Release 9.0.1 T300017537 starts
	        document.getElementById("si_back").style.display = "inline";
	        document.getElementById("si_back").src = "../../../ewdappu/images/back.jpg";
	    // Release 9.0.1 T300017537 ends
            } else {
	    // Release 9.0.1 T300017537 starts
            	 document.getElementById("si_back").style.display = "none";
	    // Release 9.0.1 T300017537 ends
            }
        }
    }
    function goForward() {
    	 parent.navigation_frame.document.getElementById("isBack").value = "back";
    var link="";
        if (currentBCItem != null && currentBCItem.next != null) {
            currentBCItem = currentBCItem.next; 
            if(currentBCItem.value.indexOf("hashId")>0)
            {
             link=currentBCItem.value.substring(currentBCItem.value.indexOf("hashId")+7);
            }        
            parent.manual_frame.location.href = currentBCItem.value+'#'+link;
	    // Release 9.0.1 T300017537 starts
            document.getElementById("si_back").style.display = "inline";
            document.getElementById("si_back").src = "../../../ewdappu/images/back.jpg";
	    // Release 9.0.1 T300017537 ends
        }
        if (currentBCItem != null) {
            if (currentBCItem.next != null) {
	    // Release 9.0.1 T300017537 starts
            	document.getElementById("si_forw").style.display = "inline";
                document.getElementById("si_forw").src = "../../../ewdappu/images/forward.jpg";
		// Release 9.0.1 T300017537 ends
            } else {
	    // Release 9.0.1 T300017537 starts
            	 document.getElementById("si_forw").style.display = "none";
		 // Release 9.0.1 T300017537 ends
            }
        }
    }
    // ---- END OF Breadcrumb/history functions -----//

    
    function toggleFind() {
        if (findState == 'hidden') {
            document.getElementById('findInput').style.display='';
            document.getElementById('search_str').focus();
            findState = 'show';
        } else {
            document.getElementById('findInput').style.display='none';
            findState = 'hidden';
        }
    }

    function entsub(event,search_str) {
        if (document.all) {
            if (window.event && window.event.keyCode == 13) {
                findString(search_str.value);
                return false;
            }
            else return true;
        } else {
            if (event && event.which == 13) {
                findString(search_str.value);
                return false;
            }
            else return true;
        }
    }
        
    function findString(str) {
    }
    
    function hideFindButtonForPDF()
    {
        if(document.all) // IE
        {
            try
            {
                this.manual_frame.document; // IE-PDF will throw error, and we wont show buttons
                //alert(control_frame.document.getElementById('findPrintButtons'));
                this.control_frame.document.getElementById('findPrintButtons').style.display='';
            } catch (ex){}
        } else // FF
        {
            var url = this.manual_frame.document.location.toString().split("?");
            var docType = url[0].substring(url[0].length-4, url[0].length).toLowerCase();
            if(docType!=".pdf")
            {
                try
                {
                    var myIndex = this.control_frame.document.body.innerHTML.indexOf("location=");
                    if(myIndex>0 && myIndex<250) // an html redirecting to pdf will have 'location=' within first 250 chars
                        this.control_frame.document.getElementById('findPrintButtons').style.display='';
                } catch (ex){}
            }
        } 
        
    } 
        
    /* You may edit the following variables */
    var window_background = "white"; // the color of the pop-up window
    var window_border = "blue"; // the border color of pop-up window
    var text_color = "black"; // the color of the text in window
    var title_color = "white"; // color of window title text
    var window_width = 200; // width of window
    var window_height = 150; // height of window
    var mozilla_opt = 1; // change to 0 to use Netscape and Firefox built-in search window
    var start_at = 0; // Change to which character you want to start with on the page if IE gives an error because of searching in menus
    // Example: start_at = 300, makes the find start at the 300th character on the page
    /* Do not edit anything below this line */

    //var ie = ((navigator.appVersion.indexOf("MSIE")!= -1)&&!window.opera)? true : false; // to detect if IE
    var ie = (document.all);
    var bookmark = new Array();
    //if (document.getElementById && !document.all)
    if (window.find)
    	var nav = 1; // to detect if netscape or firefox
    else 
    	var nav = 0;
    var t = 0;  // used for timer to move window in IE when scrolling

    var sel; // Selection object needed for Firefox
    var range; // range object needed for Firefox
    var find_again = 0;

    // The following is to capture mouse movement
    // If Netscape or Mozilla -- then set up for mouse capture
    if (!ie) 
    {
    	document.captureEvents(Event.MOUSEDOWN | Event.MOUSEMOVE | Event.MOUSEUP);
    }

    document.onmousedown = MouseDown;
    document.onmousemove = MouseMove;
    document.onmouseup = MouseUp;

    // Temporary variables to hold mouse x-y pos's
    var mousex = 0;
    var mousey = 0;

    if (ie)
    {
    	// this variable will hold all the text on the page for IE
    	// We are creating a text range from the whole document body
    	if(document.body != null){
    	var txt = document.body.createTextRange();
    	try {
    		txt = parent.manual_frame.document.body.createTextRange();
        } catch (e){
          try {
            var doc = parent.frames['manual_frame'].document;
            if (doc != null) {
            	txt = doc.body.createTextRange();
            }
          } catch (e) {
        	  txt = parent.document.body.createTextRange();
          } 
        }

    	// this variable will bookmark the last find position
    	// in an array of bookmarks so we can keep going to previous finds
    	var bookmark = new Array();
    	// bookmark the beginning of the text body
    	//bookmark[0] = txt.getBookmark();
    }
    }

    // variable to record number fo finds
    var finds = 0;    
    var error = null;
    
    function searchText(keyWord) {
    	if(document.getElementById('displaySearchResults').className == "hide"){
    		SearchWordOnClick(keyWord);
    	}else{
	if (bookmark.length==0) {
		if(ie){
			finds = 0;
			document.getElementById("currentSearchId").innerHTML == 0;
			bookmark = new Array();
			error = null;
			txt = parent.manual_frame.document.body.createTextRange();
			txt.moveStart("character", start_at);
		}else{
			finds = 0;
			document.getElementById("currentSearchId").innerHTML = 0;
			bookmark = new Array();
			error = null;
			var selection = parent.manual_frame.getSelection();
            selection.removeAllRanges();
		}
		
		
		var string = document.getElementById(keyWord).value;
		
		
		document.getElementById("keyWordValue").value = string;
		 if (string == "") {
	            alert (SEARCH_KEYWORD_MISSING);  
	            //alert("Please enter search keyword and retry.");
	            return;
	        }

		while (error == null || error == "Found") {

			if (ie) {
			//	alert("Text defined"+txt.getText());
				if(txt==null||txt=="undefined"){
					
					var text = document.body.createTextRange();
			    	try {
			    		text = parent.manual_frame.document.body.createTextRange();
			        } catch (e){
			          try {
			            var doc = parent.frames['manual_frame'].document;
			            if (doc != null) {
			            	text = doc.body.createTextRange();
			            }
			          } catch (e) {
			        	  text = parent.document.body.createTextRange();
			          } 
			         
			        }
			     
			        txt = text;
			       
			     //   alert("Text defined"+txt);
				}
				 document.getElementById("txtRange").value = txt;
				//txt.moveStart("character", start_at);
				// Bookmark this position in bookmark array at finds variable
				
				//alert(string);
				if (txt.findText(string)) {
					//alert("Text is found");
					bookmark[finds] = txt.getBookmark();
					//alert("Bookmark.length"+bookmark.length);
				//	alert("Bookmarkedword"+bookmark[finds]);
				}

				// findText is IE's javascript function to find text in
				// a text range
				//if (string) // only call findText if there is a string or IE
							// will
					// have error
					if (txt.findText(string)) // if found
					{
						//alert("Text found");
						
						// select() not only highlights the string but
						// it also moves the view to that location
						//txt.select();
						// scrollIntoView() is just a duplicate of what
						// select does by jumping to the selection, so
						// we don't realy need it.
						//txt.scrollIntoView();
						// moveStart('character') moves the position in the
						// text one character forward so that we can search
						// for the next case of string in the body
						// txt.moveStart('character');

						// collapse moves the insertion point to the end
						// of the range so we can search for the next value
						txt.collapse(false);

						error = "Found";
					} else {
						

						error = "Not found";

					}
					//alert("error"+error);
			} else // Netscape or firefox
			{
				var strFound = parent.manual_frame.find(string);
				

				if (!strFound) {
					error = "Not found";

				} else {
					//if (parent.manual_frame.getSelection) {
						//var selection = parent.manual_frame.getSelection();
						
						//if (selection.rangeCount > 0) {
							//var selectedRange = selection.getRangeAt(0);
							bookmark[finds] = "test";

						//}

					//}
				}

			}
			finds++;
			//alert("Finds"+finds);
			//alert("bookMark length"+bookmark.length);

		}
		if (bookmark.length > 1) {
			
			displaySearchResults(keyWord);

			document.getElementById("currentSearchId").innerHTML = 1;
			document.getElementById("totalSearchCountId").innerHTML = bookmark.length;
		} else if (bookmark.length == 1) {

			hideSearchResults(keyWord);
			document.getElementById('mainTable').height = "30";
		} else {
			hideSearchResults(keyWord);
			
			document.getElementById('mainTable').height = "30";
		}
		if(bookmark.length==0){
			 alert ("'" + string + "' "+SEARCH_KEYWORD_NOTFOUND);
			 finds = 0;
				document.getElementById("currentSearchId").innerHTML == 0;
				bookmark = new Array();
				error = null;
				if(!ie){
					var selection = parent.manual_frame.getSelection();
					selection.removeAllRanges();
				}
				return;
		}

              document.getElementById("keyWordValue").value = string;
              finds = 0;
              findit(keyWord);
              var myStr = document.getElementById("totalSearchCountId").innerHTML;
             // document.getElementById("totalSearchCountId").size = myStr.length;
              //document.getElementById("currentSearchId").size = myStr.length;
              //alert(myStr.length);

              
       } else {
              var initialKeywordValue = document.getElementById("keyWordValue").value;
              var changedKeywordValue = document.getElementById(keyWord).value;
              if ('' != initialKeywordValue && '' != changedKeywordValue
                            && initialKeywordValue == changedKeywordValue) {
                     if(ie){
                    	 
                            if(txt== document.getElementById("txtRange").value){
                            
                                   if(document.getElementById('displaySearchResults').className=="hide"){
                                          SearchWordOnClick(keyWord);
                                   }else{
                                   findNext(keyWord);
                                   }
                                   
                            }else{
                                   finds = 0;
                                   document.getElementById("currentSearchId").innerHTML == 0;
                                   bookmark = new Array();
                                   error = null;
                                   txt = parent.manual_frame.document.body.createTextRange();
                                   txt.moveStart("character", start_at);
                                   searchText(keyWord);
                            }
                     }else{
                            if(document.getElementById('displaySearchResults').className=="hide"){
                                   SearchWordOnClick(keyWord);
                            }else{
                            findNext(keyWord);
                            }
              }
                     
                     
              } else {
              
                     finds = 0;
                     document.getElementById("currentSearchId").innerHTML == 0;
                     bookmark = new Array();
                     error = null;
                     if(!ie){
                            var selection = parent.manual_frame.getSelection();
                            selection.removeAllRanges();
                     }else{
                            txt = parent.manual_frame.document.body.createTextRange();
                            txt.moveStart("character", start_at);
                     }
                     searchText(keyWord);
                     //if(ie){
                     //     searchText(keyWord);
                     //}
              }

	}
    	}
}

    
    function SearchWordOnClick(keyWord){
    	finds = 0;

		document.getElementById("currentSearchId").innerHTML == 0;
		bookmark = new Array();
		error = null;
	
		if(!ie){
			var selection = parent.manual_frame.getSelection();
			selection.removeAllRanges();
					
		}else{		
			txt = parent.manual_frame.document.body.createTextRange();
			txt.moveStart("character", start_at);
		}
		
		var string = document.getElementById(keyWord).value;
		
		
		document.getElementById("keyWordValue").value = string;
		 if (string == "") {
	            alert (SEARCH_KEYWORD_MISSING);  
	            //alert("Please enter search keyword and retry.");
	            return;
	        }

		while (error == null || error == "Found") {

			if (ie) {
			//	alert("Text defined"+txt.getText());
				if(txt==null||txt=="undefined"){
					
					var text = document.body.createTextRange();
			    	try {
			    		text = parent.manual_frame.document.body.createTextRange();
			        } catch (e){
			          try {
			            var doc = parent.frames['manual_frame'].document;
			            if (doc != null) {
			            	text = doc.body.createTextRange();
			            }
			          } catch (e) {
			        	  text = parent.document.body.createTextRange();
			          } 
			         
			        }
			        txt = text;
			     //   alert("Text defined"+txt);
				}
				 document.getElementById("txtRange").value = txt;
				//txt.moveStart("character", start_at);
				// Bookmark this position in bookmark array at finds variable
				
				//alert(string);
				if (txt.findText(string)) {
					//alert("Text is found");
					bookmark[finds] = txt.getBookmark();
					//alert("Bookmark.length"+bookmark.length);
				//	alert("Bookmarkedword"+bookmark[finds]);
				}

				// findText is IE's javascript function to find text in
				// a text range
				//if (string) // only call findText if there is a string or IE
							// will
					// have error
					if (txt.findText(string)) // if found
					{
						//alert("Text found");
						
						// select() not only highlights the string but
						// it also moves the view to that location
						//txt.select();
						// scrollIntoView() is just a duplicate of what
						// select does by jumping to the selection, so
						// we don't realy need it.
						//txt.scrollIntoView();
						// moveStart('character') moves the position in the
						// text one character forward so that we can search
						// for the next case of string in the body
						// txt.moveStart('character');

						// collapse moves the insertion point to the end
						// of the range so we can search for the next value
						txt.collapse(false);

						error = "Found";
					} else {
						

						error = "Not found";

					}
					//alert("error"+error);
			} else // Netscape or firefox
			{
				var strFound = parent.manual_frame.find(string);
				

				if (!strFound) {
					error = "Not found";

				} else {
					//if (parent.manual_frame.getSelection) {
						//var selection = parent.manual_frame.getSelection();
						
						//if (selection.rangeCount > 0) {
							//var selectedRange = selection.getRangeAt(0);
							bookmark[finds] = "test";

						//}

					//}
				}

			}
			finds++;
			//alert("Finds"+finds);
			//alert("bookMark length"+bookmark.length);

		}
		if(!ie){
		
		 parent.manual_frame.find(string, false, false);
		 
		}
		if (bookmark.length > 1) {
			
			displaySearchResults(keyWord);
			
			document.getElementById("currentSearchId").innerHTML = 1;
			document.getElementById("totalSearchCountId").innerHTML = bookmark.length;
						
			
		} else if (bookmark.length == 1) {

			hideSearchResults(keyWord);
			document.getElementById('mainTable').height = "30";
		} else {
			hideSearchResults(keyWord);
			
			document.getElementById('mainTable').height = "30";
		}
		if(bookmark.length==0){
			 alert ("'" + string + "' "+SEARCH_KEYWORD_NOTFOUND);
			 finds = 0;
				document.getElementById("currentSearchId").innerHTML == 0;
				bookmark = new Array();
				error = null;
				if(!ie){
					var selection = parent.manual_frame.getSelection();
					selection.removeAllRanges();
				}
				return;
		}

       document.getElementById("keyWordValue").value = string;
              finds = 0;
              if(ie){
              txt = parent.manual_frame.document.body.createTextRange();
              }
              findit(keyWord);
              var myStr = document.getElementById("totalSearchCountId").innerHTML;
              //document.getElementById("totalSearchCountId").size = myStr.length;
              //document.getElementById("currentSearchId").size = myStr.length;
              //alert(myStr.length);
              
       
    }

function displaySearchResults(keyWord) {
	if (document.getElementById(keyWord)){
		var keywordValue = document.getElementById(keyWord).value;
	}
	//document.getElementById('findInput').style.width = "35%";
	document.getElementById('displaySearchResults').className = "show";
	document.getElementById('searchkword').focus();
	document.getElementById('searchkword').value = keywordValue;
}

function hideSearchResults(kwordId) {
	if (document.getElementById(kwordId)){
	var keywordValue = document.getElementById(kwordId).value;

	}
	//document.getElementById('findInput').style.width = "20%";
	document.getElementById('displaySearchResults').className = "hide";
	document.getElementById('searchkword').focus();
	document.getElementById('searchkword').value = keywordValue;	
}

function findit(keyWord)

{

	var string = document.getElementById(keyWord).value;

	if (ie) {
		// findText is IE's javascript function to find text in
		// a text range
		if (string) // only call findText if there is a string or IE will have error
			
			if (txt.findText(string)) // if found
			{
				// select() not only highlights the string but
				// it also moves the view to that location
				txt.select();
				// scrollIntoView() is just a duplicate of what
				// select does by jumping to the selection, so
				// we don't realy need it.
				txt.scrollIntoView();
				// moveStart('character') moves the position in the
				// text one character forward so that we can search
				// for the next case of string in the body
				//txt.moveStart('character');

				// collapse moves the insertion point to the end
				// of the range so we can search for the next value
				txt.collapse(false);

				//test.innerHTML = "Found";
			} else {
				if (find_again == 0) {
					find_again = 1;
					resettext();
					findit(keyWord);
				} else {
				//	test.innerHTML = "Not found";

					find_again = 0; // reset find_again variable
				}
			}
	} else // Netscape or firefox
	{
		for(var i= 0;i<= bookmark.length-1;i++){
		      parent.manual_frame.find(string, false, true);
		    	  }
		/* var selection = parent.manual_frame.getSelection();
	      selection.removeAllRanges();
	      selection.addRange(bookmark[finds]);*/
	}
	finds++;


} // end function findit()

// This function is to find backwards by pressing the Prev button
function findprev(keyWord) {
var findTest ="";
	var initialKeywordValue = document.getElementById("keyWordValue").value;
	var changedKeywordValue = document.getElementById(keyWord).value;
	if ('' != initialKeywordValue && '' != changedKeywordValue
			&& initialKeywordValue == changedKeywordValue) {
		if (document.getElementById("currentSearchId").innerHTML == 1) {
			document.getElementById("currentSearchId").innerHTML = bookmark.length;
			if(!ie){
				for(var i= 0;i<= bookmark.length-1;i++){
				      parent.manual_frame.find(changedKeywordValue, false, false);
				    	  }
				
				findTest ="true";
			}
		} else {
			document.getElementById("currentSearchId").innerHTML = document
					.getElementById("currentSearchId").innerHTML - 1;
		}
		// put the value of the textbox in string

		// 8-9-2010 Turn DIV to hidden just while searching so doesn't find the text in the window
		//findwindow.style.visibility = 'hidden';

		if (ie) {
			// if they found only 0 or 1 occurance then don't do anything
			// because they haven't found enough to go backwards
			/*if (finds < 2) {
				findwindow.style.visibility = 'visible';
				return;
			}*/

			// Make finds variable go back to previous find
			//finds = finds - 2;  // I don't know why I have to go back 2

			// move back to previously bookmarked position

			var counter = document.getElementById("currentSearchId").innerHTML;
			txt.moveToBookmark(bookmark[counter - 1]);
			// select it
			findit(keyWord);
		} else // if netscape or firefox
	{
	var counter = document.getElementById("currentSearchId").innerHTML;
		      if(findTest!="true"){
			    	  var selection = parent.manual_frame.getSelection();
					    parent.manual_frame.find(changedKeywordValue, false, true);
				}
		      
		      
		}
	} else {
		finds = 0;
		document.getElementById("currentSearchId").innerHTML == 0;
		bookmark = new Array();
		error = null;
		if(!ie){
			var selection = parent.manual_frame.getSelection();
			selection.removeAllRanges();
		}
		searchText(keyWord);
	}
	//findwindow.style.visibility = 'visible';

} // end findprev()

// This function is to find backwards by pressing the Prev button
function findNext(keyWord) {
	var findTest = "";
	var initialKeywordValue = document.getElementById("keyWordValue").value;
	var changedKeywordValue = document.getElementById(keyWord).value;
	if ('' != initialKeywordValue && '' != changedKeywordValue
			&& initialKeywordValue == changedKeywordValue) {
		if (document.getElementById("currentSearchId").innerHTML == bookmark.length) {
			document.getElementById("currentSearchId").innerHTML =1;
			if(!ie){
			for(var i= 0;i<= bookmark.length-1;i++){
			      parent.manual_frame.find(changedKeywordValue, false, true);
			    	  }
			
			findTest ="true";
		}
		} else {
			var nextCount = document.getElementById("currentSearchId").innerHTML;
			nextCount++;
			document.getElementById("currentSearchId").innerHTML = nextCount;
		}

		if (ie) {
			// move back to previously bookmarked position
			var counter = document.getElementById("currentSearchId").innerHTML;
			txt.moveToBookmark(bookmark[counter - 1]);

			// select it
			findit(keyWord);
		} else // if netscape or firefox
		{
			/*var counter = document.getElementById("currentSearchId").value;
			test.innerHTML = window.find(string, false, true);
			sel = window.getSelection(); // get selection
			range = sel.getRangeAt(bookmark[counter - 1]); // get object
	*/	
			//test.innerHTML = parent.manual_frame.find(string, false, true);

			
			if(findTest!="true"){
		    	  var selection = parent.manual_frame.getSelection();
				     parent.manual_frame.find(changedKeywordValue, false, false);
			}
			}
	} else {
	
		finds = 0;
		document.getElementById("currentSearchId").innerHTML == 0;
		bookmark = new Array();
		error = null;
		if(!ie){
			var selection = parent.manual_frame.getSelection();
			selection.removeAllRanges();
		}
		searchText(keyWord);
	}
} // end findNext()

// This function looks for the ENTER key (13) 
// while the find window is open, so that if they user
// press ENTER it will do the find next
function checkkey(e) {
	var keycode;
	if (window.event) // if ie
		keycode = window.event.keyCode;
	else
		// if Firefox or Netscape
		keycode = e.which;

	//test.innerHTML = keycode;

	if (keycode == 13) // if ENTER key
	{
		// For some reason in IE, 
		// I have to focus on the 'NEXT' button
		// or finding by the Enter key does not always work.
		if (ie)
			document.getElementById('btn').focus();
		findit(); // call findit() function (like pressing NEXT)	
	}
} // end function checkkey()

// This function makes the findwindow DIV visible
// so they can type in what they want to search for
function show() {
	if (ie || mozilla_opt == 1) {
		//var findwindow = document.getElementById('findwindow');

		// Object to hold textbox so we can focus on it
		// so user can just start typing after "find" button
		// is clicked
		var textbox = document.getElementById('fwtext');

		// Make the find window visible
		findwindow.style.visibility = 'visible';
		//fwtext.style.visibility = 'visible';

		// Put cursor focus in the text box
		textbox.focus();

		// Call timer to move textbox in case they scroll the window
		t = setInterval('move_window();', 500);

		// Setup to look for keypresses while window is open
		document.onkeydown = checkkey;

	} else
		// if netscape or firefox
		window.find();
	// Note: Netscape and Firefox have a built in find window
	// that can be called with window.find()
	// They also have a find like IE's with self.find(string, 0, 1)
	// But I can't find any instructions on how to use it to
	// keep searching forward on "Next" button presses

} // end function show()

// This function makes the findwindow DIV hidden
// for when they click on close
function hide() {
	//var findwindow = document.getElementById('findwindow');

	findwindow.style.visibility = 'hidden';

	// turn off timer to move window on scrolling
	clearTimeout(t);

	// Make document no longer look for enter key
	document.onkeydown = null;

} // end function hide()

// This function resets the txt selection pointer to the
// beginning of the body so that we can search from the
// beginning for the new search string when somebody
// enters new text in the find box
function resettext() {
	if (ie) {
		txt = parent.manual_frame.document.body.createTextRange();
		txt.moveStart("character", start_at);
		//txt.select();
	}
	finds = 0;
} // end function reset()

// This function makes the find window jump back into view
// if they scroll while it is open or if the page automatically
// scrolls when it is hightlighting the next found text
function move_window() {
	//var findwindow = document.getElementById('findwindow');	

	// get current left, top and height of find_window
	fwtop = parseFloat(findwindow.style.top);
	fwleft = parseFloat(findwindow.style.left);
	fwheight = parseFloat(findwindow.style.height);

	// get current top and bottom position of browser screen
	if (document.documentElement.scrollTop) // Needed if you use doctype loose.htm
		current_top = document.documentElement.scrollTop;
	else if(document.body)
		current_top = document.body.scrollTop;
	
	if (document.documentElement.clientHeight) {
		if (document.documentElement.clientHeight > document.body.clientHeight)
			current_bottom = document.body.clientHeight + current_top;
		else
			current_bottom = document.documentElement.clientHeight
					+ current_top;
	} else
		current_bottom = document.body.clientHeight + current_top;

	// get current left and right position of browser
	if (document.documentElement.scrollLeft) // Needed if you use doctype loose.htm
		current_left = document.documentElement.scrollLeft;
	 else if(document.body)
		current_left = document.body.scrollLeft;
	if (document.documentElement.clientWidth) {
		if (document.documentElement.clientWidth > document.body.clientWidth)
			current_right = document.body.clientWidth + current_left;
		else
			current_right = document.documentElement.clientWidth + current_left;
	} else
		current_right = document.body.clientWidth + current_left;

	//test.innerHTML = current_right + ',' + current_left;

	// Only move window if it is out of the view
	if (fwtop < current_top) {
		// move window to current_top
		findwindow.style.top = current_top + 'px';
	} else if (fwtop > current_bottom - fwheight) {
		// move to current_bottom
		findwindow.style.top = current_bottom - fwheight + 'px';
	}

	// Only move left position if out of view
	if (fwleft < current_left || fwleft > current_right) {
		findwindow.style.left = current_left + 'px';
	}

	var test = document.getElementById('test');
	test.innerHTML = 'find window: ' + fwtop + ' curr_bottom: '
			+ current_bottom;

} // end function move_window()

function MouseDown(e) {
	if (over == 1)
		DivID = 'findwindow';

	if (over) {
		if (ie) {
			objDiv = document.getElementById(DivID);
			objDiv = objDiv.style;
			mousex = event.offsetX;
			mousey = event.offsetY;
		} else // if Mozilla or Netscape 
		{
			objDiv = document.getElementById(DivID);
			mousex = e.layerX;
			mousey = e.layerY;
			return false;
		}
	}
}

function MouseMove(e) {

	// get current top 
	if (document.documentElement.scrollTop) // Needed if you use doctype loose.htm
     	current_top = document.documentElement.scrollTop;
		else if(document.body)
		current_top = document.body.scrollTop;

	// get current left
	if (document.documentElement.scrollLeft) // Needed if you use doctype loose.htm
		current_top = document.documentElement.scrollLeft;
	 else if(document.body)
		current_left = document.body.scrollLeft;

	
	if (objDiv) {
		if (ie) {
			objDiv.pixelLeft = event.clientX - mousex + current_left;
			objDiv.pixelTop = event.clientY - mousey + current_top;
			return false;
		} else // if Mozilla or Netscape
		{
			objDiv.style.left = (e.pageX - mousex) + 'px';
			objDiv.style.top = (e.pageY - mousey) + 'px';
			return false;
		}
	}
} // end function MouseMove(e)

//
//
//
function MouseUp() {
	objDiv = null;
}

// Create findwindow DIV but make it invisible
// It will be turned visible when user clicks on
// the "Find on this page..." button

function create_div(dleft, dtop, dwidth, dheight) {
	if (document.documentElement.scrollTop) // Needed if you use doctype loose.htm
		current_top = document.documentElement.scrollTop;
	else
		current_top = document.body.scrollTop;

	if (document.getElementById('findwindow')) {
		findwindow = document.getElementById('findwindow');
		//win_iframe = document.getElementById('win_iframe');
	} else {
		findwindow.id = "findwindow";
		findwindow.style.position = 'absolute';
		//document.body.appendChild(findwindow);
		document.body.insertBefore(findwindow, document.body.firstChild);
		findwindow.className = 'findwindow';
		findwindow.style.visibility = 'hidden';
	}

	findwindow.style.backgroundColor = window_background;
	findwindow.style.border = '2px solid ' + window_border;
	findwindow.style.color = text_color;
	findwindow.style.width = window_width + 'px';
	findwindow.style.height = +window_height + 'px';
	findwindow.style.top = 0;
	findwindow.style.left = 0;
	findwindow.style.padding = '0px';
	findwindow.style.zIndex = 2000;
	findwindow.style.fontSize = '14px';
	findwindow.style.overflowX = 'hidden';
	//findwindow.style.display = "block";

	// This part creates the title bar
	findwindow.innerHTML = '<div style="text-align: center' + ';width: '
			+ (window_width - 20) + 'px' + ';cursor: move' // turn mouse arrow to move icon
			+ ';color: ' + title_color + ';border: 1px solid ' + text_color
			+ ';background-color: ' + window_border + ';float: left'
			+ ';" onmouseover="over=1;" onmouseout="over=0;">'
			+ 'Find Window</div>';
	// This part creates the closing X
	findwindow.innerHTML += '<div onclick="hide();" style="text-align: center'
			+ ';width: ' + (16) + 'px' + ';cursor: default' // make mouse arrow stay an arrow instead of turning to text arrow
			+ ';font-weight: bold' + ';background-color: red'
			+ ';border: 1px solid ' + text_color + ';float: right' + ';">'
			+ 'X' // write the letter X
			+ '</div><br />\n';
	// This part creates the instructions and the "find" button
	findwindow.innerHTML += '<div id="window_body" style="padding: 5px;">'
			+ 'Type in text to find: '
			+ '<input type="text" size="25" maxlength="25" id="fwtext"'
			+ ' onchange="resettext();">'
			+ '<input type="button" value="Find Prev" onclick="findprev();">'
			+ '<input id="btn" type="button" value="Find Next" onclick="findit();">'
			+ '</div>\n' + '<div id="test"><br /></div>';

} // end function create_div()

// This part creates a visible button on the HTML page to
// where the script is pasted in the HTML code
//   document.write('<input type="button" value="Find on this page..."'
//   	+ ' onclick="show();">');

// Create the DIV
var findwindow = document.createElement("div");
// create_div();

// over variable is whether mouse pointer is over the DIV to move
var over = 0;

// Object to hold findwindow for MouseMove
var objDiv = null;

// ID of DIV for MouseMove functions
var DivID = null;

var test = document.getElementById('test');

function clearSerachFun(kwordId) {
//	alert("Inside ClearSearch Function");
	var keyWordVal = document.getElementById(kwordId).value;
	
	if (null == keyWordVal || "" == keyWordVal) {
		hideSearchResults(kwordId);
		//document.getElementById('mainTable').height = "30";
	}
}


function checkForEnter(event, obj){
	var e = event || window.event;
		if(e.keyCode == 13){
			//SearchWordOnClick(obj);
			if(e.preventDefault) 
				e.preventDefault();
			else
				e.returnValue = false;
			searchText(obj);
		}    
}
//Release 9.0.1 T300017537 starts
	var modelFilter = null;
	var pdrFilter = null;
	var homeFilter = null;
	var pdrSelected = null;
	var isChange = false;
	var targetLength = 0;
	//Checks if pdr contains any special characters or empty spaces
	function checkSpecialChar(pdr){
		var resultPdr = ''; 
		var pdrArray  = pdr.split("-");
		if(pdrArray != "" && pdr != null){
			if(pdrArray[0] != "" && pdrArray[0] != null){
				resultPdr= pdrArray[0]+"-";
				 if(pdrArray[1] != "" && pdrArray[1] != null){
					 if(pdrArray[1].charCodeAt  != 194 && pdrArray[1].charCodeAt  != 160){
						 resultPdr= pdrArray[0]+"-"+pdrArray[1];
					 }
				 }
			}
		}	
		return resultPdr;	
	}	
	// loads the drop down filters
	function loadFilters(val, selectedMY, selectedPDR, home,pcdChange,keyword,targetPDRLength) {
		if (window.XMLHttpRequest) {
			xhttp = new XMLHttpRequest();
		} else // IE 5/6
		{
			xhttp = new ActiveXObject("Microsoft.XMLHTTP");
		}
		xhttp.open("GET", val, false);
		xhttp.send();
		xmlDoc = xhttp.responseXML;
		
		modelFilter = selectedMY;
		pdrFilter = selectedPDR;
		homeFilter = home;
		targetLength = targetPDRLength;
		var tocdata = xmlDoc.getElementsByTagName("tocdata");
		
		//To get the model year data from term-data element of toc.xml
		var yeardataColl = xmlDoc.getElementsByTagName("yeardata");
		for ( var i = 0; i < yeardataColl.length; i++) {
			var my = {};
			my.YEAR = yeardataColl[i].getAttribute('model-year');
			my.START_DATE = yeardataColl[i].getAttribute('startdate');
			my.END_DATE = yeardataColl[i].getAttribute('enddate');
			ewdMYList[i] = my;
		}
		//To get the pdr data from term-data element of toc.xml
		var periodColl = xmlDoc.getElementsByTagName("period");
		for ( var i = 0; i < periodColl.length; i++) {
			var pd = {};
			var pdr = periodColl[i].getAttribute('display-name');
			pd.YEAR = checkSpecialChar(pdr);
			pd.START_DATE = periodColl[i].getAttribute('startdate');
			pd.END_DATE = periodColl[i].getAttribute('enddate');
			ewdPeriodList[i] = pd;
		}
		if (tocdata.length > 0) {
			isFatToc = "true";
		} else {
			document.getElementById("staticDiv").style.display = "none";
		}
		createMYFilterDropdown("my", "mdlYearSelect", selectedMY);
		createPCDFilterDropdown(selectedMY, "pcd", "pcdSelect",pcdChange,false);
	}
	
	//loads the model year drop down
	function createMYFilterDropdown(selId, divId, selectedModelYear) {
		var str = '';
		var modelArray = [];
		var j = 0;
		for ( var i = 0; i < ewdMYList.length; i++) {
			modelArray[j] = ewdMYList[i].YEAR;
			j++;
		}
		modelArray.sort(function(a, b) {
			return b - a;
		});
	
		str = '<select id="' + selId
				+ '" name="myfilter" onchange="changeMY()" style="width:155px;">';
		for ( var i = 0; i < modelArray.length; i++) {
			if (selectedModelYear == modelArray[i]) {
				str = str + '<option selected value="' + modelArray[i] + '">'
						+ modelArray[i] + '</option>';
			} else {
				str = str + '<option  value="' + modelArray[i] + '">'
						+ modelArray[i] + '</option>';
			}
		}
		str = str + '</select>';
		document.getElementById(divId).innerHTML = str;
		
	}
	function filterMY(selMY){
		var selectedMY = [];
		for(var i = 0; i< ewdMYList.length; i++){
			if(selMY == ewdMYList[i].YEAR){
				selectedMY = ewdMYList[i];
				break;
			}
		}
		return selectedMY;
	}
	
	function validatePDR(pdr){
		var pdrArray = pdr.split("-");
        if(pdrArray[0] != "" && pdrArray[0] != null){
        		pdr = pdrArray[0]+"-";
        	if(pdrArray[1] != "" && pdrArray[1] != null && (pdrArray[1].charCodeAt(0) != 194)&&(pdrArray[1].charCodeAt(0) != 160) && (pdrArray[1].indexOf("&nbsp") == -1)){
        		pdr = pdr+pdrArray[1];
        	}
        }
        return pdr;
	}
	function isValidPdr(pdr){
		var pdrArray = pdr.split("-");
        if(pdrArray[0] != "" && pdrArray[0] != null){
        	if(pdrArray[1] != "" && pdrArray[1] != null && (pdrArray[1].charCodeAt(0) != 194)&&(pdrArray[1].charCodeAt(0) != 160) && (pdrArray[1].indexOf("&nbsp") == -1)){
        		return true;
        	}
        }
        return false;
	}
	function filterPDR(selPcd){
		var selectedPCD = [];
		for(var i = 0; i< ewdPeriodList.length; i++){
			if(selPcd == ewdPeriodList[i].YEAR){
				selectedPCD = ewdPeriodList[i];
				break;
			}
		}
		return selectedPCD;
	}
	//loads the pdr drop down
	function createPCDFilterDropdown(selMY,selId,divId,pcdChange,isChangeMY) {
		var listA = [];
		var j = 0;
		var selectedMY = filterMY(selMY);
		for (var i = 0; i < ewdPeriodList.length; i++) {
			var flagInfo = false;
			if ((selectedMY.START_DATE != null && selectedMY.START_DATE != "" && selectedMY.START_DATE != "undefined") &&
					(selectedMY.END_DATE != null && selectedMY.END_DATE != "") &&
						(ewdPeriodList[i].START_DATE  != null && ewdPeriodList[i].START_DATE  != "") &&
							(ewdPeriodList[i].END_DATE != null && ewdPeriodList[i].END_DATE != "")){

				if((selectedMY.END_DATE > ewdPeriodList[i].START_DATE ) && (selectedMY.START_DATE < ewdPeriodList[i].END_DATE)){
					flagInfo = true;
				}
			}else if((selectedMY.START_DATE != null && selectedMY.START_DATE != "" && selectedMY.START_DATE != "undefined") &&
					(selectedMY.END_DATE == null || selectedMY.END_DATE == "") &&
						(ewdPeriodList[i].START_DATE != null && ewdPeriodList[i].START_DATE != "") &&
							(ewdPeriodList[i].END_DATE != null && ewdPeriodList[i].END_DATE != "")){

				if(ewdPeriodList[i].END_DATE > selectedMY.START_DATE){
					flagInfo = true;
				}   
			}else if((selectedMY.START_DATE != null && selectedMY.START_DATE != "" && selectedMY.START_DATE != "undefined") &&
					(selectedMY.END_DATE != null && selectedMY.END_DATE != "") &&
						(ewdPeriodList[i].START_DATE != null && ewdPeriodList[i].START_DATE != "") &&
							(ewdPeriodList[i].END_DATE == null || ewdPeriodList[i].END_DATE == "")){

				if(ewdPeriodList[i].START_DATE < selectedMY.END_DATE){
					flagInfo = true;

				}
			}else if((selectedMY.START_DATE != null && selectedMY.START_DATE != "" && selectedMY.START_DATE != "undefined") &&
					(selectedMY.END_DATE == null || selectedMY.END_DATE == "") &&
						(ewdPeriodList[i].START_DATE != null && ewdPeriodList[i].START_DATE != "") &&
							(ewdPeriodList[i].END_DATE == null || ewdPeriodList[i].END_DATE == "")){

				flagInfo = true;
			}
			if(flagInfo){
				listA[j] = ewdPeriodList[i];
				j++;
			}
		}
		
		
		document.getElementById(divId).innerHTML = "";
		
		listA.sort(function(a, b) {
			return b - a;
		});
		
		var pdrDataArray = [];
		var pdrEndDateEmptyArray = [];
		var finalPdrArray = [];
		var k=0;
		var m=0;
		for(var i=0; i < listA.length; i++){
			var validPdr = isValidPdr(listA[i].YEAR);
			if(validPdr){
				pdrDataArray[k] = listA[i];
				k++;
			}else{
				pdrEndDateEmptyArray[m] = listA[i];
				m++;
			}
		}
		function compareDESC(a, b)
		{
		    return parseInt(b.END_DATE) - parseInt(a.END_DATE);
		}
		pdrDataArray.sort(compareDESC);
		finalPdrArray = pdrEndDateEmptyArray.concat(pdrDataArray);
		var listLength = listA.length;
		var str = '<select id="'+selId+'" onchange="this.style.backgroundColor=this.options[this.selectedIndex].style.backgroundColor;changePCD()" style="width:155px;">';
		if(pcdChange != "true"){
			var PDR = "PDR";
			if(listLength > 1){
				str = str + '<option style="background-color:#FF0000;" selected value="PDR">'+
				PDR+'</option>';
			} 
		}
		
		for (i = 0; i < finalPdrArray.length; i++) {
			if(checkSpecialChar(finalPdrArray[i].YEAR) == checkSpecialChar(pdrFilter)){
				flag = "true";
				str = str + '<option style="background-color:#FFFFFF;" selected value="' + checkSpecialChar(pdrFilter) + '">'+ checkSpecialChar(pdrFilter) + '</option>';
			}else{
				str = str + '<option  style="background-color:#FFFFFF;" value="' + finalPdrArray[i].YEAR + '">'+finalPdrArray[i].YEAR+'</option>' ;
			}
		}
		str = str + '</select>';
		document.getElementById(divId).innerHTML = str;
		var pcdSel = document.getElementById("pcd");
		if(pcdChange != "true" && listLength > 1){
			pcdSel.style.backgroundColor = "#FF0000";
		}
		if(targetLength == 1){
			removeValue(pcdSel);
		}
		var pdr;
		if(pdrFilter == "null"){
			pdr = pcdSel.options[pcdSel.selectedIndex].innerHTML;
		}else{
			removeValue(pcdSel);
			pdr = pdrFilter;
		}
        pdr = validatePDR(pdr);
    	var selectedPCD = filterPDR(pdr);
		var url = window.location.href;
    	var regex = new RegExp("[&\\?]" + "modelyear" + "=");
        var modelYearStartlen = url.indexOf("&modelyear");
        var pcdStartlen = url.indexOf("&pcd");
        var startlen;
        if(pcdStartlen != -1){
	        if(modelYearStartlen > pcdStartlen){
	        	startlen = pcdStartlen;
	        }else{
	        	startlen = modelYearStartlen;
	        }
        }else{
        	startlen = modelYearStartlen;
        }
        var endlen = url.length;
        var replaceStr = url.substring(startlen,endlen);
		if(listLength == 1){
			if(modelFilter == 0 && homeFilter == "recentDocs"){
	        	this.location.href = url+"&modelyear="+selMY+"&pcd="+pdr+"&startDate="+selectedPCD.START_DATE;
	        	
	        }else if(isChangeMY == true){
		        if(regex.test(url)){
		        	url = url.replace(replaceStr,"&modelyear="+selMY+"&pcd="+selectedPCD.YEAR+"&startDate="+selectedPCD.START_DATE);
		        	this.location.href =  url;
		        }else{
		        	this.location.href = url+"&modelyear="+selMY+"&pcd="+selectedPCD.YEAR+"&startDate="+selectedPCD.START_DATE;
		        }
	        }
		}else if(listLength > 1 && isChangeMY == true){
			if(regex.test(url)){
	        	url = url.replace(replaceStr,"&modelyear="+selMY+"&pcd="+null);
	        	this.location.href =  url;
	        }else{
	        	this.location.href = url+"&modelyear="+selMY+"&pcd="+null;
	        }
		}
	}

	function changeMY(){
		//Changes to reset the back and forward on change of modelyear
		parent.control_frame.document.getElementById("si_back").style.display = "none";
		parent.control_frame.document.getElementById("si_forw").style.display = "none";
		//Changes to reset the back and forward on change of modelyear
		parent.control_frame.document.getElementById("isExpanded").value = "false";
		//var selectedPCD = [];
		var mySel = document.getElementById("my");
		var my = mySel.options[mySel.selectedIndex].innerHTML;
		disableLanguageButtons();
		var url = window.location.href;
        var regex = new RegExp("[&\\?]" + "modelyear" + "=");
        var modelYearStartlen = url.indexOf("&modelyear");
        var pcdStartlen = url.indexOf("&pcd");
        var startlen;
        if(pcdStartlen != -1){
	        if(modelYearStartlen > pcdStartlen){
	        	startlen = pcdStartlen;
	        }else{
	        	startlen = modelYearStartlen;
	        }
        }else{
        	startlen = modelYearStartlen;
        }
        var endlen = url.length;
        var replaceStr = url.substring(startlen,endlen);
        var hrefStartLen;
        var hrefEndLen;
        var hrefReplaceStr;
        var hrefVal = parent.manual_frame.location.href;
		var hrefQueryParam = "";
		if(hrefVal.indexOf("xhtml") != -1){
			parent.control_frame.document.getElementById("isExpandedOnLoad").value = "true";
			hrefVal = hrefVal.substring(hrefVal.indexOf("xhtml"),hrefVal.indexOf("?"));
			hrefQueryParam= "&href="+hrefVal;
		}
		var manFrameHref = parent.control_frame.document.getElementById("manFrameHref").value;
		var manFrameSubString = "";
		if(manFrameHref != ""){
			var manFrameStartLen = manFrameHref.indexOf("xhtml");
			var manFrameEndLen = manFrameHref.indexOf("?");
			manFrameSubString = manFrameHref.substring(manFrameStartLen,manFrameEndLen);
			manFrameSubString = "&href="+manFrameSubString;
		}
		hrefStartLen = url.indexOf("&href");
    	hrefEndLen= url.indexOf(".html");
    	hrefReplaceStr = url.substring(hrefStartLen,hrefEndLen+5);
		if(url.indexOf("href") != -1){
        	if(manFrameSubString != ""){
        		url = url.replace(hrefReplaceStr,manFrameSubString);
        	}else if(hrefQueryParam != ""){
        		url = url.replace(hrefReplaceStr,hrefQueryParam);
        	}
		}else{
			url = url+manFrameSubString;
		}
		parent.control_frame.document.getElementById("isTreeExpanded").value = "";
		parent.control_frame.document.getElementById("isTreeExpandedOnLoad").value;
		
		if(regex.test(url)){
        	url = url.replace(replaceStr,"&modelyear="+my+"&pcd="+null+"&pcdChangeBack=true"+"&random="+Math.random()); //Rel 16.01 - Siviewer Defect fixes 
        	if(url.indexOf("href") == -1){
        		url = url+manFrameSubString;
        	}
        	this.location.href =  url;
        }else{
        	this.location.href = url+"&modelyear="+my+"&pcd="+null+"&pcdChangeBack=true"+"&random="+Math.random(); //Rel 16.01 - Siviewer Defect fixes 
        }
        parent.control_frame.document.getElementById("changeFilter").value = "changed";
        if(parent.control_frame.document.getElementById("findInput")!= null && parent.control_frame.document.getElementById("findInput")!="undefined"){
        	bookmark = new Array();
        	finds = 0;
        	//parent.control_frame.document.getElementById('findInput').style.width = "20%";
        	parent.control_frame.document.getElementById('displaySearchResults').className = "hide";
        	
        } 
	}		
	
	function disableLanguageButtons()
	{
		var Sel = document.getElementById("my");
		var selmy = Sel.options[Sel.selectedIndex].value;
		
		var en_year;
		var es_year;
		var fr_year;
		var endisable=false;
		var esdisable=false;
		var frdisable=false;
		var locale;
		if(parent.control_frame.document.getElementById("locale"))
		{
	    locale=parent.control_frame.document.getElementById("locale").value;
		}
		if(parent.control_frame.document.getElementById("en_year"))
		{
		var en_year=parent.control_frame.document.getElementById("en_year").value;
		}
		if(parent.control_frame.document.getElementById("es_year"))
		{
		var es_year=parent.control_frame.document.getElementById("es_year").value;
		}
		if(parent.control_frame.document.getElementById("fr_year"))
		{
		var fr_year=parent.control_frame.document.getElementById("fr_year").value;
		}
		
		if(en_year==null || en_year=="")
		{
		endisable=true;
		}
		else if(en_year!=null && en_year.indexOf(selmy)==-1)
	    {
		endisable=true;
	    }
		
		
		if(es_year==null || es_year=="")
		{
		esdisable=true;
		}
		else if(es_year!=null && es_year.indexOf(selmy)==-1)
	    {
		esdisable=true;
	    }
		
		if(fr_year==null || fr_year=="")
		{
		frdisable=true;
		}
		else if(fr_year!=null && fr_year.indexOf(selmy)==-1)
	    {
		frdisable=true;
	    }
		
		  var imgpath=parent.control_frame.document.getElementById('en').src;
		  var modifedPath=imgpath.substring(0,imgpath.lastIndexOf("/")+1);
		if(locale=='en')
		{
			parent.control_frame.document.getElementById("en").src=modifedPath+"english_selected.png";
			parent.control_frame.document.getElementById("en").style.cursor = "pointer";
		}
		else
		{
			if(endisable)
		    {
				 
				  parent.control_frame.document.getElementById('en').src=modifedPath+'en_unavailable.png';
				  parent.control_frame.document.getElementById("en").style.cursor = "auto";
				  parent.control_frame.document.getElementById('en').style.disable=true;	
		    }
			else
			{
				 parent.control_frame.document.getElementById('en').src=modifedPath+'english_available.png';
				 //parent.control_frame.document.getElementById('en').style.disable=false;
				 parent.control_frame.document.getElementById("en").style.cursor = "pointer";
			}
		}
		
		
		if(locale=='es')
		{
			parent.control_frame.document.getElementById("es").src=modifedPath+"spanish_selected.png";
			parent.control_frame.document.getElementById("es").style.cursor = "pointer";
		}
		else
		{
			
			if(esdisable)
		      {
				 
				  parent.control_frame.document.getElementById('es').src=modifedPath+'es_unavailable.png';
				  parent.control_frame.document.getElementById("es").style.cursor = "auto";
				  parent.control_frame.document.getElementById('es').style.disable=true;	
		     }
			    else
				{
				parent.control_frame.document.getElementById('es').src=modifedPath+'spanish_available.png';
				//parent.control_frame.document.getElementById('es').style.disable=false;
				parent.control_frame.document.getElementById("es").style.cursor = "pointer";
				}
			
		}
		if(locale=='fr')
		{
			parent.control_frame.document.getElementById("fr").src=modifedPath+"french_selected.png";
			parent.control_frame.document.getElementById("fr").style.cursor = "pointer";
		}
		else
			{
			
			if(frdisable)
		    {
				 
				  parent.control_frame.document.getElementById('fr').src=modifedPath+'fr_unavailable.png';
				  parent.control_frame.document.getElementById('fr').style.cursor = "auto";
				  //parent.control_frame.document.getElementById('fr').style.disable=true;	
		    }
			else
				{
				
				parent.control_frame.document.getElementById('fr').src=modifedPath+'french_available.png';
				//parent.control_frame.document.getElementById('fr').style.disable=false;	
				parent.control_frame.document.getElementById("fr").style.cursor = "pointer";
				
				}
			}
		
	}
	function removeValue(dateSel) {
		for (var i=0; i<dateSel.length; i++){
		  if (dateSel.options[i].value == 'PDR' )
			  {
			  	dateSel.remove(i);
			  	dateSel.style.backgroundColor = "#FFFFFF";
			  }
		  }
	}
	
	function changePCD(){
		//Changes to reset the back and forward on change of PDR
		parent.control_frame.document.getElementById("si_back").style.display = "none";
		parent.control_frame.document.getElementById("si_forw").style.display = "none";
		//Changes to reset the back and forward on change of PDR
		parent.control_frame.document.getElementById("isExpanded").value = "false";
		isChange = true;
		var pcdSel = document.getElementById("pcd");
		var pcd = pcdSel.options[pcdSel.selectedIndex].value;
		removeValue(pcdSel);
		filterProcessPCD(pcd,isChange);
		parent.control_frame.document.getElementById("isTreeExpanded").value = "";
		parent.control_frame.document.getElementById("isTreeExpandedOnLoad").value;
		parent.control_frame.document.getElementById("changeFilter").value = "changed";
		if(parent.control_frame.document.getElementById('findInput')!= null && parent.control_frame.document.getElementById('findInput')!="undefined"){
        	bookmark = new Array();
        	finds = 0;
        	//parent.control_frame.document.getElementById('findInput').style.width = "20%";
        	parent.control_frame.document.getElementById('displaySearchResults').className = "hide";
        } 
	}
	
	function filterProcessPCD(selPCD,isChange){
		var id = document.getElementById("currentDoc").value;
		var mySel = document.getElementById("my");
		var modelyear = mySel.options[mySel.selectedIndex].innerHTML;
		if(id != ""){
			var href = document.getElementById(id).href;
			var startLen = href.indexOf("xhtml");
			var endLen = href.indexOf("?");
			index = href.substring(startLen,endLen);
		}
		var selectedPCD = [];
		selectedPCD = filterPDR(selPCD);
		var url = window.location.href;
        var regex = new RegExp("[&\\?]" + "modelyear" + "=");
        var modelYearStartlen = url.indexOf("&modelyear");
        var pcdStartlen = url.indexOf("&pcd");
        var startlen;
        if(pcdStartlen != -1){
	        if(modelYearStartlen > pcdStartlen){
	        	startlen = pcdStartlen;
	        }else{
	        	startlen = modelYearStartlen;
	        }
        }else{
        	startlen = modelYearStartlen;
        }
        var endlen = url.length;
        var replaceStr = url.substring(startlen,endlen);
        var hrefStartLen;
        var hrefEndLen;
        var hrefReplaceStr;
        var hrefVal = parent.manual_frame.location.href;
		var hrefQueryParam = "";
		if(hrefVal.indexOf("xhtml") != -1){
			parent.control_frame.document.getElementById("isExpandedOnLoad").value = "true";
			hrefVal = hrefVal.substring(hrefVal.indexOf("xhtml"),hrefVal.indexOf("?"));
			hrefQueryParam= "&href="+hrefVal;
		}
		var manFrameHref = parent.control_frame.document.getElementById("manFrameHref").value;
		var manFrameSubString = "";
		if(manFrameHref != ""){
			var manFrameStartLen = manFrameHref.indexOf("xhtml");
			var manFrameEndLen = manFrameHref.indexOf("?");
			manFrameSubString = manFrameHref.substring(manFrameStartLen,manFrameEndLen);
			manFrameSubString = "&href="+manFrameSubString;
		}
		hrefStartLen = url.indexOf("&href");
    	hrefEndLen= url.indexOf(".html");
    	hrefReplaceStr = url.substring(hrefStartLen,hrefEndLen+5);
		if(url.indexOf("href") != -1){
        	if(manFrameSubString != ""){
        		url = url.replace(hrefReplaceStr,manFrameSubString);
        	}else if(hrefQueryParam != ""){
        		url = url.replace(hrefReplaceStr,hrefQueryParam);
        	}
		}else{
			url = url+manFrameSubString;
		}
       
		if(regex.test(url)){
        	url = url.replace(replaceStr,"&modelyear="+modelyear+"&pcd="+selectedPCD.YEAR+"&startDate="+selectedPCD.START_DATE+"&pcdChange="+isChange+"&pcdChangeBack="+isChange+"&random="+Math.random()); //Rel 16.01 - Siviewer Defect fixes 
        	if(url.indexOf("href") == -1){
        		url = url+manFrameSubString;
        	}
    		this.location.href = url;
        }else{
        	this.location.href = url+"&modelyear="+modelyear+"&pcd="+selectedPCD.YEAR+"&startDate="+selectedPCD.START_DATE+"&pcdChange="+isChange+"&pcdChangeBack="+isChange+"&random="+Math.random(); //Rel 16.01 - Siviewer Defect fixes 
        }
	}
	
	function get(name){
		var url = window.location.toString();
		var result = null;
		//get the parameters
		url.match(/\?(.+)$/); var params = RegExp.$1;
		// split up the query string and store in an // associative array
		var params = params.split("&"); 
		var queryStringList = {}; 
		for(var i=0;i<params.length;i++) {   
			var tmp = params[i].split("=");  
			if(name == tmp[0])
			{
				result = tmp[1];
			 return result;
			}}  
		}
	
     function readTocXml(tocPath,selectedDoc){
    	 var fromyear = 0;
    	 var toyear = 0;
    	 if (window.XMLHttpRequest) {    
     	    var xmlhttp=new XMLHttpRequest();
     	    xmlhttp.open("GET",tocPath,false);  
     	    xmlhttp.send();

     	    var xmlDoc=xmlhttp.responseXML;
     	    var itemList = xmlDoc.getElementsByTagName("item");
     	    for(var i=0; i < itemList.length ;i++){
     	    	var attributes = itemList[i].attributes;
     	    	var hrefVal= attributes.getNamedItem("href").value;
     	    	if(hrefVal != ""){
     	    		if(hrefVal.indexOf(selectedDoc) != -1){
     	    			if(xmlDoc.getElementsByTagName("tocdata")[i] != null){
	     	    			var tocDataList = xmlDoc.getElementsByTagName("tocdata")[i].attributes;
	     	    			fromyear= tocDataList.getNamedItem("fromyear").value;
	     	    			toyear= tocDataList.getNamedItem("toyear").value;
	     	    			break;
	     	    		}
     	    		}
     	    	}
     	    }
     	}
    	 return fromyear+"-"+toyear;
     }
     //Release 9.0.1 T300017537 ends
   
