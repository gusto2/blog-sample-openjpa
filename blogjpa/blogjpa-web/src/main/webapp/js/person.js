/* 
 * Module to implement Person ModelList with REST cals
 */

YUI.add('person', function(Y) {

                        Y.Person = Y.Base.create('person', Y.Model, [Y.ModelSync.REST], {
                            root: '/person',
                            url: '/person/{id}',
                            // overriding a default savw fn , just for demo purposes
                            save: function(callback) {
                                Y.io('/person', {
                                    method: 'POST',
                                    data: this.toJSON(),
                                    context: this,
                                    on: {
                                        success: function(tid, xhr) {
                                            // doesn't want to load the +id attr
//                                    this.parse(xhr.responseText);
                                            var resp = JSON.parse(xhr.responseText);
                                            this.set('_id', resp._id);
                                            this.set('id', resp.id);
//                                    this.parse(resp);
                                            this.fire("change");
                                            if(callback)
                                                callback();
                                        },
                                        failure: function(tid, xhr) {
                                            this.fire("error", xhr);
                                            if(callback) 
                                             {
                                                 var m = (xhr.responseText)?xhr.responseText:xhr.statusText;
                                                callback(m);
                                             }
                                        }
                                    }
                                });
                            },
                            ATTR: {
                                id: {},
                                _id: {},
                                name: {value: null},
                                born: {value: null},
                                rrn: {value: null}
                            }
                        });

                        Y.PersonList = Y.Base.create('persons', Y.ModelList, [Y.ModelSync.REST], {
//                     By convention `Y.User`'s `root` will be used for `Y.Users` as well.
                            root: '/person',
                            url: '/person',
                            model: Y.Person
                        });
}, '0.0.1', {requires: ['model','model-list','model-sync-rest','io','json-parse']});


