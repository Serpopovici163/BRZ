function isIE(){
    	var myNav = navigator.userAgent.toLowerCase();
    	return (myNav.indexOf("msie") != -1)? parseInt(myNav.split("msie")[1]) : false;
    }
    function checkMLink(){
        var manual_text = parent.manual_frame.document;
        var modelYear = "";
        var pdr = "";
        var enddate = "";
        var isTargetExists = false;
        if(window.frames['navigation_frame'].document.getElementById("my")){
        	modelYear = window.frames['navigation_frame'].document.getElementById("my").value;
        	var mLink = getElementsByClassName(manual_text,"mlink");
        	if(mLink.length > 0){
        		if(window.frames['navigation_frame'].document.getElementById("pcd")){
        			pdr = window.frames['navigation_frame'].document.getElementById("pcd").value;
        		}
        		var pdrArray = pdr.split("-");
        		if(pdrArray != "" && pdrArray != null && "undefined" != typeof pdrArray){
        			if(pdrArray[0] != "" && pdrArray[0] != null && pdrArray[0] != "undefined"){
        				var pdrModelArray = pdrArray[0].split("/");
        				startdate = pdrModelArray[1]+ pdrModelArray[0];
        			}
        			if(pdrArray[1] != "" && pdrArray[1] != null && pdrArray[1] != "undefined" && (pdrArray[1].charCodeAt(0)  != 194 && pdrArray[1].charCodeAt(0)  != 160)){
        				var pdrModelArray = pdrArray[1].split("/");
        				enddate = pdrModelArray[1]+ pdrModelArray[0];
        			}
        		}
        		//Commented for R 9.2.1 - T300018174 - Start
        		//var docModelYearInfo = parent.control_frame.document.getElementById("docToYear").value;
        		//if(docModelYearInfo != "" && docModelYearInfo.indexOf("-") != -1){
        			//var docModelYear = docModelYearInfo.split("-");
        			//var mlinkStartYear = docModelYear[0];
        			//var mlinkEndYear = docModelYear[1];
        		//Commented for R 9.2.1 - T300018174 - End
        			for(var i=0; i<mLink.length ; i++){
        				var spanTag = mLink[i].getElementsByTagName("span");
        				var linkData = mLink[i].getElementsByTagName("a");
        				var imgData = mLink[i].getElementsByTagName("img");
        				for(var j=0; j< spanTag.length; j++){
        					var flagInfo = false;
        					var pdrFlag = false;
        					var spanClassName = null;
        					if(isIE()){
        						spanClassName =  spanTag[j].getAttribute("className");
        						if(spanClassName==null||spanClassName=='')               //T300019272 Defect- Info button enable changes for IE 
        							{
        							spanClassName =  spanTag[j].getAttribute("class");
        							}
        					}else{
        						spanClassName =  spanTag[j].getAttribute("class");
        					}
        					if(spanClassName != null){
        						var mlinkPdr = spanClassName.split(" ");
        						//R 9.2.1 - T300018174 - Start
        						if(mlinkPdr[0] != "" && mlinkPdr[0] != "undefined"){
        							var mlinkMYStart = mlinkPdr[0].split(":");
        							var mlinkStartYear = mlinkMYStart[1];
        							var mlinkEndYear = "";
        							if(mlinkPdr[1]  != ""  && 'undefined' != typeof mlinkPdr[1] && 'undefined' != mlinkPdr[1]){
        								var mlinkMYEnd = mlinkPdr[1].split(":");
        								if("myto" == mlinkMYEnd[0]){
        									mlinkEndYear = mlinkMYEnd[1];
        								}
        								else if("startdate" == mlinkMYEnd[0]){
        									mlinkStartDate = mlinkEndYear[1];
        								}
        							}
        							var mlinkEndDate = "";
        							if(mlinkPdr[2]  != ""  && 'undefined' != typeof mlinkPdr[2] && 'undefined' != mlinkPdr[2]){
        								var mlinkStart = mlinkPdr[2].split(":");
           								if("startdate" == mlinkStart[0]){
           									mlinkStartDate = mlinkStart[1];
        								}
        								else if("enddate" == mlinkStart[0]){
        									mlinkEndDate = mlinkStart[1];
        								}
        							}
        							
       								if(mlinkPdr[3]  != ""  && 'undefined' != typeof mlinkPdr[3] && 'undefined' != mlinkPdr[3]){
       									var mlinkEnd = mlinkPdr[3].split(":");
       									mlinkEndDate = mlinkEnd[1];
       								}
       								//R 9.2.1 - T300018174 - End
       								if(modelYear >= mlinkStartYear && (mlinkEndYear == "" || modelYear <= mlinkEndYear)){
       									pdrFlag = true;
       									if(pdr.indexOf("PDR") == -1){
       										if ((startdate != null && startdate != "" && startdate != "undefined") &&
       												(enddate != null && enddate != "") &&
       													(mlinkStartDate != null && mlinkStartDate != "") &&
       														(mlinkEndDate != null && mlinkEndDate != "")){
       											if((mlinkEndDate > startdate) && (mlinkStartDate < enddate)){
       												flagInfo = true;
       											}
       										}else if((startdate != null && startdate != "" && startdate != "undefined") &&
       												(enddate == null || enddate == "") &&
       													(mlinkStartDate != null && mlinkStartDate != "") &&
       														(mlinkEndDate != null && mlinkEndDate != "")){
       											if(mlinkEndDate > startdate){
       												flagInfo = true;
       											}
       										}else if((startdate != null && startdate != "" && startdate != "undefined") &&
       												(enddate != null && enddate != "") &&
       													(mlinkStartDate != null && mlinkStartDate != "") &&
       														(mlinkEndDate == null || mlinkEndDate == "")){
       											if(mlinkStartDate < enddate){
       												flagInfo = true;
       											}
       										}else if((startdate != null && startdate != "" && startdate != "undefined") &&
       												(enddate == null || enddate == "") &&
       													(mlinkStartDate != null && mlinkStartDate != "") &&
       														(mlinkEndDate == null || mlinkEndDate == "")){
       											flagInfo = true;
       										}
       									}
       								}
        						}
        						if(mlinkPdr.length > 0){
        							if(pdr.indexOf("PDR") != -1){
        								if(!pdrFlag){
        									imgData[j].style.display = "none";
        								}else{
        									linkData[j].href="javascript:alert('Please select a Production Date Range (PDR) to proceed.')";
        									if(typeof imgData[j] != 'undefined')
        									{
        										isTargetExists = true;
        										imgData[j].src="/t3Portal/stylegraphics/infodisable.gif";
        										imgData[j].style.borderColor = "grey";
        									}
        								}
        							}else{
        								if(!flagInfo){
										//R 9.2.1 - T300018177 - Start
										if(typeof imgData[j] != 'undefined'){
        										imgData[j].style.display= "none";
										}
										else{
											var hrefVal = linkData[j].href;
        										linkData[j].onmousedown = function (){
        											readTOCXml(this.href);
        										};
										}
										//R 9.2.1 - T300018177 - End
        								}else{
    										isTargetExists = true;
    										//R 9.2.1 - T300018177 - Start
        									var hrefVal = linkData[j].href;
        									linkData[j].onmousedown = function (){
        										readTOCXml(this.href);
        									};
        									//R 9.2.1 - T300018177 - End
        								}
        							}
        						}
        					}
        				}
        			}
        			if(!isTargetExists){
        				for(var i=0; i<mLink.length ; i++){
        					var spanTag = mLink[i].getElementsByTagName("span");
        					var linkData = mLink[i].getElementsByTagName("a");
        					var imgData = mLink[i].getElementsByTagName("img");
        					for(var j=0; j< spanTag.length; j++){
        						if(pdr.indexOf("PDR") != -1){
        							linkData[j].href="javascript:alert('Please select a Production Date Range (PDR) to proceed.')";
        						}else{
        							linkData[j].href="javascript:alert('Target document is not within the currently selected model year and/or production date range.')";
        						}
        						if(typeof imgData[j] != 'undefined')
        						{
        							imgData[j].style.display= "inline";
        							imgData[j].src="/t3Portal/stylegraphics/infodisable.gif";
        							imgData[j].style.borderColor = "grey";
        						}
        					}
        				}
        			}
        		//}	//Commented for R 9.2.1 - T300018174
        	}
        }	
        }
  //R 9.2.1 - T300018177 - Start
    function readTOCXml(hrefVal){
   	 var fromyear = 0;
   	 var toyear = 0;
   	 var fromdate = 0;
   	 var todate = 0;
   	 var tocPath = parent.control_frame.document.getElementById("pathVal").value;
   	 var selectedDoc = hrefVal.substring(hrefVal.indexOf("xhtml"),hrefVal.indexOf("?"));
   	 var documentUrl = parent.manual_frame.location.href;
   	 var docUrl = documentUrl.substring(documentUrl.indexOf("xhtml"),documentUrl.indexOf("?"));
   	 //alert(docUrl);
   	 //alert(selectedDoc);
   	 if (window.XMLHttpRequest) {    
    	    var xmlhttp=new XMLHttpRequest();
    	    xmlhttp.open("GET",tocPath,false);  
    	    xmlhttp.send();

    	    var xmlDoc=xmlhttp.responseXML;
    	    var itemList = xmlDoc.getElementsByTagName("item");
    	    for(var i=0; i < itemList.length ;i++){
    	    	var attributes = itemList[i].attributes;
    	    	hrefVal= attributes.getNamedItem("href").value;
    	    	if(hrefVal != ""){
    	    		if(hrefVal.indexOf(selectedDoc) != -1){
    	    			if(xmlDoc.getElementsByTagName("tocdata")[i] != null){
	     	    			var tocDataList = xmlDoc.getElementsByTagName("tocdata")[i].attributes;
	     	    			fromyear = tocDataList.getNamedItem("fromyear").value;
	     	    			toyear = tocDataList.getNamedItem("toyear").value;
	     	    			fromdate = tocDataList.getNamedItem("fromdate").value;
	     	    			todate = tocDataList.getNamedItem("todate").value;
	     	    			break;
	     	    		}
    	    		}
    	    	}
    	    }
    	}
   	 var docYearInfn = fromyear + "-" + toyear + ":" + fromdate + "-" + todate;
    	var flagInfo = false;
    	var mlinkStartYear = "";
		var mlinkEndYear = "";
		var mlinkStartDate = "";
		var mlinkEndDate = "";
		var modelYear = "";
		var pdr = "";
		var startdate = "";
		var enddate = "";
		if(window.frames['navigation_frame'].document.getElementById("my")){
        	modelYear = window.frames['navigation_frame'].document.getElementById("my").value;
		}
		
		if(window.frames['navigation_frame'].document.getElementById("pcd")){
			pdr = window.frames['navigation_frame'].document.getElementById("pcd").value;
		}
		var pdrArray = pdr.split("-");
		if(pdrArray != "" && pdrArray != null && "undefined" != typeof pdrArray){
			if(pdrArray[0] != "" && pdrArray[0] != null && pdrArray[0] != "undefined"){
				var pdrModelArray = pdrArray[0].split("/");
				startdate = pdrModelArray[1]+ pdrModelArray[0];
			}
			if(pdrArray[1] != "" && pdrArray[1] != null && pdrArray[1] != "undefined" && (pdrArray[1].charCodeAt(0)  != 194 && pdrArray[1].charCodeAt(0)  != 160)){
				var pdrModelArray = pdrArray[1].split("/");
				enddate = pdrModelArray[1]+ pdrModelArray[0];
			}
		}
		
    	if(docYearInfn != "" && docYearInfn.indexOf(":") != -1){
			var yrDateArray = docYearInfn.split(":");
			if(yrDateArray[0]  != ""  && 'undefined' != typeof yrDateArray[0] && 'undefined' != yrDateArray[0]){
				if(yrDateArray[0] != "" && yrDateArray[0].indexOf("-") != -1){
					var modelYrArray = yrDateArray[0].split("-");
					if(modelYrArray[0]  != ""  && 'undefined' != typeof modelYrArray[0] && 'undefined' != modelYrArray[0]){
						mlinkStartYear = modelYrArray[0];
					}
					if(modelYrArray[1]  != ""  && 'undefined' != typeof modelYrArray[1] && 'undefined' != modelYrArray[1]){
						mlinkEndYear = modelYrArray[1];
					}
				}
			}
			if(yrDateArray[1]  != ""  && 'undefined' != typeof yrDateArray[1] && 'undefined' != yrDateArray[1]){
				if(yrDateArray[1] != "" && yrDateArray[1].indexOf("-") != -1){
					var dateArray = yrDateArray[1].split("-");
					if(dateArray[0]  != ""  && 'undefined' != typeof dateArray[0] && 'undefined' != dateArray[0]){
						mlinkStartDate = dateArray[0];
					}
					if(dateArray[1]  != ""  && 'undefined' != typeof dateArray[1] && 'undefined' != dateArray[1]){
						mlinkEndDate = dateArray[1];
					}
				}
			}
			
		}
    	if(modelYear >= mlinkStartYear && (mlinkEndYear == "" || modelYear <= mlinkEndYear) && selectedDoc != "" && selectedDoc != docUrl){
			if ((startdate != null && startdate != "" && startdate != "undefined") &&
					(enddate != null && enddate != "") &&
						(mlinkStartDate != null && mlinkStartDate != "") &&
							(mlinkEndDate != null && mlinkEndDate != "")){
				if((mlinkEndDate > startdate) && (mlinkStartDate < enddate)){
					flagInfo = true;
				}
			}else if((startdate != null && startdate != "" && startdate != "undefined") &&
					(enddate == null || enddate == "") &&
						(mlinkStartDate != null && mlinkStartDate != "") &&
							(mlinkEndDate != null && mlinkEndDate != "")){
				if(mlinkEndDate > startdate){
					flagInfo = true;
				}
			}else if((startdate != null && startdate != "" && startdate != "undefined") &&
					(enddate != null && enddate != "") &&
						(mlinkStartDate != null && mlinkStartDate != "") &&
							(mlinkEndDate == null || mlinkEndDate == "")){
				if(mlinkStartDate < enddate){
					flagInfo = true;
				}
			}else if((startdate != null && startdate != "" && startdate != "undefined") &&
					(enddate == null || enddate == "") &&
						(mlinkStartDate != null && mlinkStartDate != "") &&
							(mlinkEndDate == null || mlinkEndDate == "")){
				flagInfo = true;
			}
		}
    	else {
    		flagInfo = false;
    	}
    	
    	if(flagInfo == true){
    		var is_firefox = navigator.userAgent.toLowerCase().indexOf('firefox') > -1;
    		if(is_firefox){
    			parent.manual_frame.location.href = hrefVal;
    		}
    	}
    	
    	if(!flagInfo) {
    		var manual_text = parent.manual_frame.document;
    		var mLink = getElementsByClassName(manual_text,"mlink");
    		if(mLink.length > 0){
    			for(var k=0; k<mLink.length ; k++){
    				var spanTag = mLink[k].getElementsByTagName("span");
    				var linkData = mLink[k].getElementsByTagName("a");
    				var imgData = mLink[k].getElementsByTagName("img");
    				for(var j=0; j< spanTag.length; j++){
    					var refVal = linkData[j].href;
    					var docInfo = refVal.substring(refVal.indexOf("xhtml"),refVal.indexOf("?"));
    					if(null != linkData[j].href && "" != linkData[j].href && (linkData[j].href.indexOf(selectedDoc) != -1) ||
    							docInfo == docUrl) {
    						var is_chrome = navigator.userAgent.toLowerCase().indexOf('chrome') > -1;
    						var is_firefox = navigator.userAgent.toLowerCase().indexOf('firefox') > -1;

    						if(is_chrome || is_firefox ){
	    						linkData[j].href = "javascript:void(0)";	
	    		    			linkData[j].click = alert('Sorry. This document does not match the filter criteria you have chosen.');
	    						linkData[j].href = "#";	
    						}else{
    							alert('Sorry. This document does not match the filter criteria you have chosen.');		
    						}
    						return false;

    					}
    					else
    						continue;
    				}
    			}
    		}
    	}
    }
  //R 9.2.1 - T300018177 - End
     function getElementsByClassName(node, classname) {
          var a = [];
          var re = new RegExp('(^| )'+classname+'( |$)');
          var els = node.getElementsByTagName("*");
          for(var i=0,j=els.length; i<j; i++)
               if(re.test(els[i].className))a.push(els[i]);
          	        return a;
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