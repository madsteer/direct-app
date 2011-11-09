var rightSidebarData = {};
var currentProjectName = "";
var contestData;

// JavaScript Document
$(document).ready(function(){

    var Sys = {};
    var ua = navigator.userAgent.toLowerCase();


    $.ajax({
        url: '/direct/customerProjectsAjaxAction',
        async: false,
        dataType: 'json',
        success:
            function(data) {
                var returnData = data.result['return'];
                rightSidebarData = returnData.projects;
                contestData = returnData.contests;
                currentProjectName = $.trim(returnData.currentProjectName);
            }
    });


    adjustContestListHeight = function() {
           var rows_height = 0;
           var contests_nbre = 0;

           /* get the height of the 10 first rows ( one contest per row)*/
           $("#contestsTable TBODY").children().each(function() {
               if (contests_nbre < 10)
                   rows_height += $(this).height();
               contests_nbre++;
           });

           if (contests_nbre > 10) {
               $(".contestsContent").height(rows_height);

               // Chrome
               if (ua.match(/chrome\/([\d.]+)/) != null && ua.match(/chrome\/([\d.]+)/)[1].split('.')[0] > 2) {
                   $(".contestsContent").height(rows_height + 20);
               }

               // Safari
               if (ua.match(/version\/([\d.]+).*safari/) != null && ua.match(/version\/([\d.]+).*safari/)[1].split('.')[0] > 3) {
                   $(".contestsContent").height(rows_height + 20);
               }

               // IE 7
               if ($.browser.msie && $.browser.version == 7.0) {
                   $(".contestsContent").height(rows_height + 20);
               }

               // IE 8
               if ($.browser.msie && $.browser.version == 8.0) {
                   $(".contestsContent").height(rows_height + 20);
               }

               $(".contestsContent TABLE").css("width", "250px");

               return rows_height + 20;
           } else {
               $(".contestsContent").css("width", "250px");
           }

           return 0;
       }

    //------------------------------------------------- Contests List
    /* sort contest by title */
    sortTitle = function(){
         var sorting = [[1,0]];
        $("#contestsTable").trigger("sorton",[sorting]);
        $("#contestsTable tbody tr").removeClass("even");
        $("#contestsTable tbody tr").removeClass("even");
        $("#contestsTable tbody tr").each(function(){
            $("#contestsTable tbody tr:odd").addClass("even");
        });
    }

    filterProject = function(){
        if ($("#dropDown1").attr("display") == "none") {
            showHideProjectList();
        }
        var typedText = $(".projectSelectMask .inputSelect>input")[0].value;

        $("#dropDown1>ul>li").each(function() {
            if ($(this).find("a")[0].innerHTML.toLowerCase().indexOf(typedText.toLowerCase()) == -1) {
                $(this).css('display', 'none');
            } else {
                $(this).css('display', '');
            }
        });
    }
    
    /* sort contest by status */
    sortStatus = function(){
        var sorting = [[0,0]];
        $("#contestsTable").trigger("sorton",[sorting]);
        $("#contestsTable tbody tr").removeClass("even");
        $("#contestsTable  tbody tr").removeClass("even");
        $("#contestsTable tbody tr").each(function(){
            $("#contestsTable tbody tr:odd").addClass("even");
        });
    }
    
    /* sort contest by type */
    sortType = function(){
        var sorting = [[2,0]];
        $("#contestsTable").trigger("sorton",[sorting]);
        $("#contestsTable tbody tr").removeClass("even");
        $("#contestsTable tbody tr").removeClass("even");
        $("#contestsTable tbody tr").each(function(){
            $("#contestsTable tbody tr:odd").addClass("even");
        });
    }
    
    /* get the selected index and sort the contests table */
    sortTable = function(mySelect){
        var selected = mySelect.options[mySelect.selectedIndex].value;
        
        if( selected == "title" )
            sortTitle();
        else if(selected == "status")
            sortStatus();
        else 
            sortType();
    }

    showHideProjectList = function(){
        $("#dropDown1").slideToggle(100);
        $("#sortTableBy").toggle();

        if($(".projectSelectMask .contestsDropDown UL").height() > 200) {
            $(".projectSelectMask .contestsDropDown UL").css('width', 233);
        }
    }

    showHideCustomerList = function(){
        var  contestsDropDown = $(".customerSelectMask .contestsDropDown");
        if(contestsDropDown.is(":hidden")){
            $(".customerSelectMask .contestsDropDown").hide();
        }
        contestsDropDown.slideToggle(100);
    }

    $(".customerSelectMask .inputSelect input").focus(function(){
        showHideCustomerList();
    });
    $(".customerSelectMask .inputSelect a").click(function(){
        showHideCustomerList();
    });

    var updateCustomerDropDown  = function(dropDownWrapper,items){
        var dropDown = dropDownWrapper.find(".contestsDropDown ul");
        var input = dropDownWrapper.find(".inputSelect input");
        dropDown.find("li").remove();
        input.val("");
        $.each(items,function(index,item){
            var li = $("<li><a class='longWordsBreak' href='#'></a></li>");
            li.data("id",item.id);
            li.find("a").text(item.value);
            if(index == 0){
                input.val(item.value);
            }
            li.appendTo(dropDown);
        })
        dropDown.find("li:odd").addClass("even");
    }

    var updateProjectDropDown  = function(dropDownWrapper,items){
        var dropDown = dropDownWrapper.find(".contestsDropDown ul");
        var input = dropDownWrapper.find(".inputSelect input");
        dropDown.find("li").remove();
        input.val("");
        var hasCurrentProject = false;
        $.each(items,function(index,item){
            var li = $("<li><a class='longWordsBreak' href='/direct/projectOverview?formData.projectId=" + item.id + "'></a></li>");
            li.data("id",item.id);
            li.find("a").text(item.value);
            if($.trim(item.value) == currentProjectName){
                input.val(item.value);
                hasCurrentProject = true;
            }
            li.appendTo(dropDown);
        })

        if (!hasCurrentProject) input.val("Select a project here");

        if (typeof isInProjectScope != 'undefined' && !isInProjectScope) {

            if (hasCurrentProject) {
                if ((undefined != currentProjectContests) && $("#contestsTable tbody tr").length <= 0) {
                    $("#contestsTable tbody").html(currentProjectContests);
                    var newHeight =  adjustContestListHeight();
                    if (newHeight > 0) {
                        $(".jScrollPaneContainer").height(newHeight);
                    }
                    $('.contestsContent').jScrollPane({
                        scrollbarWidth: 17,
                        showArrows: true
                    });
                }
            } else {
                // clear the contests if needed
                if ($("#contestsTable tbody tr").length > 0) {
                    currentProjectContests = $("#contestsTable tbody").html();
                    $("#contestsTable tbody").html('');
                    $(".jScrollPaneContainer").height(233);
                    $('.contestsContent').jScrollPane({
                        scrollbarWidth: 17,
                        showArrows: true
                    });
                }
            }


        }

        dropDown.find("li:odd").addClass("even");
    }


    function compareProject(projectA, ProjectB) {
        if (projectA.value.toLowerCase() < ProjectB.value.toLowerCase())
            return -1;
        if (projectA.value.toLowerCase() > ProjectB.value.toLowerCase())
            return 1;
        return 0;
    }

    function compareCustomer(customerA, customerB) {
        if (customerA.value.toLowerCase() == 'all customers') return -1;
        if (customerB.value.toLowerCase() == 'all customers') return 1;

        if (customerA.value.toLowerCase() < customerB.value.toLowerCase())
            return -1;
        if (customerA.value.toLowerCase() > customerB.value.toLowerCase())
            return 1;
        return 0;
    }

    var getCustomers = function(){
        var arr = [{"id":"","value":"All Customers"}];
        var count = 0;
        var noCustomer;
        if (typeof rightSidebarData != 'undefined' && rightSidebarData) {
            for(var p in rightSidebarData){
                 if(typeof(rightSidebarData[p])!="function"){
                    count ++;
                    var obj = new Object();
                     obj.value = p;
                     obj.id = rightSidebarData[p]["id"];
                     obj.projects = rightSidebarData[p]["projects"];

                     if (!(obj.id == "none")) {
                        arr.push(obj);
                     } else {
                        noCustomer = obj;
                     }
                 }
            }
        }

        arr.sort(compareCustomer);

        if (undefined != noCustomer) {
            arr.push(noCustomer);
        }

        if (count == 1) { arr.shift(); }

        return arr;
    }
    var customerList = getCustomers();
    updateCustomerDropDown($(".customerSelectMask"),customerList);


    $(".customerSelectMask  UL LI").click(function() {
        var mask = $(this).parents(".customerSelectMask");
        mask.find("input").val($(this).find("a").text());
        mask.find(".contestsDropDown").slideToggle(100);
        updateProjectDropDown($(".projectSelectMask"), getProjects($(this).data("id")));
        if ($("#activeContests").length > 0) {
            $.activeContestsDataTable.fnFilter($(this).data("id"), 10);
            $.activeContestsDataTable.fnSort([
                [3,'desc']
            ]);
            var customer = "";
            if ($(this).data("id") != "0" && $(this).data("id") != "") {
                customer = $(this).text();
            }
            updateBreadcrumb(customer);
        }
        return false;
    })

    var getProjects = function(id){
        var arr = [];
        $.each(customerList,function(index,item){
            var projects = item.projects;
            if(id == "" || id == "0"){
                for(var p in projects){
                     if(typeof(projects[p])!="function"){
                        var obj = new Object();
                         obj.id = p;
                         obj.value = projects[p];
                         arr.push(obj);
                     }
                }
            }else{
                if(item.id == id){
                    for(var p in projects){
                         if(typeof(projects[p])!="function"){
                            var obj = new Object();
                             obj.id = p;
                             obj.value = projects[p];
                             arr.push(obj);
                         }
                    }
                }
            }
        });

        arr.sort(compareProject);

        return arr;
    }
    updateProjectDropDown($(".projectSelectMask"),getProjects(""));

    var count = 0;
    $("#contestsContent").html('');

    $.each(contestData, function(key, value){
        var templateData = value;
        templateData.i = count++;

        var row = '<tr onclick="document.location.href=' + '\'/direct/contest/detail.action?projectId=' + templateData.id + "';this.style.cursor='pointer';\"" +
        " class='" +  "'>"  + 
        '<td width="15%" class="status">' +
            '<span class="' +  templateData.status + '"><span>a</span></span>' +
        '</td><td width="70%">' +
            templateData.title +
        '</td><td width="15%" class="type"><div style="display:table-cell;vertical-align:middle;">' +
                '<img alt="' + templateData.type + '"src="/images/' + templateData.type + '_small.png"/>' + '</div></td></tr>';

        $("#contestsContent").append(row);
    });
    /*---------------------- Show the scrollbar when the number of contests is more than 10----*/


    adjustContestListHeight();

    /* Stylished scrollbar*/
    $('.contestsContent').jScrollPane({
        scrollbarWidth: 17,
        showArrows: true
    });


    if ($("#contestsTable tbody tr").length > 0) {
        $("#contestsTable").tablesorter();
        sortTitle();
        $("#contestsTable tbody tr").removeClass("even");
        $("#contestsTable tbody tr:odd").addClass("even");
    } else {
       $(".contestsContent").css('width', '250px');
    }


    /*-------------------------- Show/hide the dropdown list --*/

    /*------------------------- hover state of the dropdown list  --*/
    
    $(".contestsDropDown UL LI").live('mouseover', function(){
            $(this).addClass("hover");
    });
    
    $(".contestsDropDown UL LI").live('mouseout', function(){
            $(this).removeClass("hover");
    });
    
    /*------------------------- hover state of the contests table --*/
    $("table#contestsTable tr").live('mouseover', function(){
        $(this).addClass("highlight");
    });
    
    $("table#contestsTable tr").live('mouseout', function(){
        $(this).removeClass("highlight");
    });
    
    
    /*------------------------- show or hide rows functionnality in dashboard.html --*/
    // we will show just the first rows_nbre rows 
    $("TABLE.rowsToHide").each(function(){
        
        var table_id = $(this).attr("id");
        var hide_row = false;
        
        $("#"+table_id+" TBODY TR").each(function(){
                                                  
                if( this.className.search("hideStart") != -1 ){
                        hide_row = true;
                }
                    
                if( hide_row )
                        $(this).addClass("hide");
        })
    });
    
    
    showHideRows = function(myLink, tableId){
        
        if( $(myLink).html() == "View More" ){ //when the user click the view more link we will show the hidden rows
            $("#"+tableId+" TBODY TR").each(function(){
                $(this).removeClass("hide");
            });
            
             $(myLink).html("Hide Rows");
             $(myLink).addClass("less");
             
        }else{ //when the user click the hide rows link we will hide some rows
            var hide_row = false;
            $("#"+tableId +" TBODY TR").each(function(){
                    
                    if( this.className.search("hideStart") != -1 ){
                        hide_row = true;
                    }
                    
                    if( hide_row )
                        $(this).addClass("hide");
            });
            
            $(myLink).html("View More");
            $(myLink).removeClass("less");
        }
        
    }
    
    /*----------------- projects table hover --*/
    $("table.project TR").mouseover(function(){
            $(this).addClass("hover");
    });
    
    $("table.project TR").mouseout(function(){
            $(this).removeClass("hover");
    });


    if ($.browser.msie && $.browser.version == 7.0) {
        $(".contestsContent").css("overflow-x", "hidden");
    }
    
    /*------------------------------------------------------------ Calendar --*/
    

    
    var date = new Date();
    var d = date.getDate();
    var m = date.getMonth();
    var y = date.getFullYear();
    
    $('#calendar').fullCalendar({
        header: {
            left: 'prev',
            center: 'title',
            right: 'next'
        },
        editable: false,
        dayNamesShort: ['Sunday','Monday','Tuesday','Wednesday','Thursday','Friday','Saturday'],
        events: [
            {
                title: '<a href=# class=launch onmouseover=showPopup(this,\'contestLaunch1\')>Contest Launch</a>',
                start: new Date(y, m, 8)
            },
            {
                title: '<a href=# class=launch onmouseover=showPopup(this,\'contestLaunch4\')>Spec Post</a>',
                start: new Date(y, m, 8)
            },
            {
                title: '<a href=# class=task onmouseover=showPopup(this,\'contestLaunch5\')>Task</a>',
                start: new Date(y, m, 8)
            },
            {
                title: '<a href=# class=launch onmouseover=showPopup(this,\'contestLaunch2\')>Review Complete</a>',
                start: new Date(y, m, 11)
            },
            {
                title: '<a href=# class=forum onmouseover=showPopup(this,\'contestLaunch6\')>Forum Post</a>',
                start: new Date(y, m, 11)
            },
            {
                title: '<a href=# class=forum onmouseover=showPopup(this,\'contestLaunch7\')>Forum Post</a>',
                start: new Date(y, m, 11)
            },
            {
                title: '<a href=# class=task onmouseover=showPopup(this,\'contestLaunch8\')>Task</a>',
                start: new Date(y, m, 11)
            },
            {
                title: '<a href=# class=launch onmouseover=showPopup(this,\'contestLaunch3\')>Review Pending</a>',
                start: new Date(y, m, 13)
            },
            {
                title: '<a href=# class=forum onmouseover=showPopup(this,\'contestLaunch9\')>Forum Post</a>',
                start: new Date(y, m, 13)
            },
            {
                title: '<a href=# class=task onmouseover=showPopup(this,\'contestLaunch10\')>Task</a>',
                start: new Date(y, m, 13)
            },
            {
                title: '<a href=# class=milestone onmouseover=showPopup(this,\'contestLaunch11\')>Milestone Feed back</a>',
                start: new Date(y, m, 17)
            },
            {
                title: '<a href=# class=task onmouseover=showPopup(this,\'contestLaunch12\')>Task 1</a>',
                start: new Date(y, m, 17)
            },
            {
                title: '<a href=# class=task onmouseover=showPopup(this,\'contestLaunch13\')>Task 2</a>',
                start: new Date(y, m, 17)
            },
            {
                title: '<a href=# class=winner onmouseover=showPopup(this,\'contestLaunch14\')>Winner Announcement</a>',
                start: new Date(y, m, 20)
            }
        ]
    });
});    
