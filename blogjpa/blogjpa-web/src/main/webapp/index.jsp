<!DOCTYPE html> 
<%
    String context = this.getServletContext().getContextPath();
%>
<html>
    <head>
        <title>BlogJPA web client</title>
        <meta charset="utf-8">
        <meta name="viewport" content="user-scalable=no, initial-scale=1, maximum-scale=1, minimum-scale=1, width=device-width, height=device-height, target-densitydpi=device-dpi">
        <!--<link rel="stylesheet" href="http://yui.yahooapis.com/pure/0.4.2/pure-min.css">-->   
        <link rel="stylesheet" href="<%=context%>/css/test.css" />
        <link rel="stylesheet" href="<%=context%>/css/bootstrap.css" />
        <script src="http://yui.yahooapis.com/3.17.2/build/yui/yui-min.js"></script>
        <!--<script src="./js/person.js"></script>-->
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    </head>
    <body class="yui3-skin-sam">
        <div id="message" class="section"></div>
        <div id="app">

        </div>
        <script src="<%=context%>/js/person.js"></script>
        <script type="text/javascript">
            YUI().use('app', 'node', 'event', 'escape',
                    'model', 'model-list', 'model-sync-rest', 'io', 'json-parse',
                    'datatable', 'person',
                    function(Y) {

                        function message(msg) {
                            var node = Y.one('#message');
                            if (!node && msg && msg != '')
                                alert(msg);
                            else
                                node.setHTML(Y.Escape.html(msg));
                        }

// view
                        Y.PersonListView = Y.Base.create("personListView", Y.View, [], {
                            model: new Y.PersonList(),
                            initializer: function() {
                                var model = this.model;
                                if (!model) {
                                    message('personlist model is not defined');
                                    return;
                                };
                                // Re-render this view when the model changes, and destroy this view when
                                // the model is destroyed.
//                                model.after('change', this.updateTable, this);
                                model.after('destroy', this.destroy, this);
                            },
                            render: function()
                            {
                                var viewSrc = Y.Node.create(Y.one('#mainViewContent').getHTML());
                                var container = this.get('container');
                                container.setHTML('');
                                container.appendChild(viewSrc);
                                var context = this;

                                var dataTable = new Y.DataTable({
                                    data: this.model,
                                    columns: [
                                        {
                                            key: 'name',
                                            label: 'name',
                                            allowHTML: true,
                                            formatter: function(o) {
                                                return '<a href="#/detail/' + o.data.id + '">' + o.data.name + '</a>';
                                            }
                                        },
                                        'address'],
                                    recordType: Y.Person
                                });
                                var model = this.model;

                                dataTable.data = model;
                                model.load(function(e) {
                                    if (!e) {
                                        var tableNode = container.one('#personTable');
                                        dataTable.render(tableNode);
                                        message('');
                                    }
                                    else {
                                        message(e);
                                    }
                                });

                            },
                            events: {
                                '#updateBtn': {click: function(evt) {
                                        var model = this.model;
                                        var context = this;
                                        model.load(function(e) {
                                            if (!e) {
                                                message('');
                                            }
                                            else {
                                                message(e);
                                            }
                                        });
                                    }},
                                '#createBtn': {click: function(evt) {
                                        var model = this.model;
                                        var name = Y.one('#personName');
                                        var address = Y.one('#personAddress');
                                        var nameValue = name.get('value');
                                        var context = this;
                                        if (!nameValue || nameValue == '') {
//                                            context.fire("error", {type: 'action', data: 'Name is mandatory'});
                                            message('Name is mandatory');
                                            return;
                                        }
                                        var person = new Y.Person({
                                            name: nameValue, address: address.get('value')});

                                        person.save(function(e) {
                                            if (!e) {
                                                //message('Created person id: ' + person.get('id'));
                                                name.set('value', '');
                                                address.set('value', '');
//                                                model.add(person);
                                                model.load(function(e2) {
                                                    if (e2) {
                                                        message(e2);
                                                    }
                                                    else message('');
                                                });

                                            } else {
                                                message(e);
                                            }
                                        });
                                    }}

                            }
                        });

                        Y.PersonDetailView = Y.Base.create("personDetailView", Y.View, [], {
                            model: new Y.Person(),
                            render: function() {

                                var viewSrc = Y.Node.create(Y.one('#detailViewContent').getHTML());
                                var container = this.get('container');
                                container.setHTML('');
                                container.appendChild(viewSrc);

                                var personId = this.get('personId');
                                var model = this.model;
                                var context = this;
                                if (!personId)
                                {
                                    message('Person id not specified');
                                    return;
                                }
                                model.set('id', personId);
                                model.load(function(e) {
                                    if (!e) {
                                        var person = model.toJSON();
                                        Y.one('#personDetailId').setHTML(Y.Escape.html(person.id));
                                        Y.one('#personDetailName').setHTML(Y.Escape.html(person.name));
                                        Y.one('#personDetailAddress').setHTML(Y.Escape.html(person.address));
                                    }
                                    else {
                                        message(e);
                                    }
                                });
                            },
                            events: {
                                '#deleteBtn': {click: function(evt) {
                                        var model = this.model;
                                        var context = this;
                                        model.destroy({delete: true}, function(e) {
                                            if (!e) {
                                                app.save('/index.jsp');
                                            }
                                            else {
                                                message(e);
                                            }
                                        });
                                    }
                                }}
                        });

                        var app = new Y.App({
                            views: {
                                home: {type: 'PersonListView'},
                                detail: {type: 'PersonDetailView', parent: 'home'}
                            },
                            viewContainer: '#app',
                            transitions: true,
                            serverRouting: true,
                            root: '<%=context%>'
                        });

                        app.route('/index.jsp', function(req, resp, data) {
                            app.showView('home');
                        });
                        app.route('/detail/:id', function(req, resp, data) {
                            var personId = req.params.id;
                            app.showView('detail',
                                    {personId: personId},
                            {update: true});
                        });
                        app.set('navigateOnHash', true);
                        app.on("error", function(e) {
                            if (e.data)
                                message(e.data);
                            else
                                message(e);
                        })
                        app.render().dispatch();

                    }
            );
        </script>

        <script type="text/x-template" id="mainViewContent">
            <div class="section">
            name: <input type="text" required id="personName"><br>
            address: <input type="text" id="personAddress"><br>
            <button id="createBtn" class="btn">Create</button>
            </div>
            <div id="personTable" class="section"></div>
            <div><button id="updateBtn" class="btn">Refresh</button></div>
        </script>

        <script type="text/x-template" id="detailViewContent">
            <div class="section">
            <a href="#/index.jsp">Back to list</a><br>
            id: <span id="personDetailId"></span><br>
            name: <span id="personDetailName"></span><br>
            address: <span id="personDetailAddress"></span><br>
            <button id="deleteBtn" class="btn">Delete</button>
            </div>
        </script>        
    </body>
</html>