/* 
 * Module to implement Person ModelList with REST cals
 */

YUI.add('person', function(Y) {

// model
                        Y.Person = Y.Base.create('person', Y.Model, [Y.ModelSync.REST], {
                            root: '/services/blog/person',
                            url: '/services/blog/person/{id}',
                            // overriding a default savw fn , just for demo purposes
                            parse: function(response) {
                                var resp = Y.JSON.parse(response);
                                var person = resp.person;
                                if (!person) {
                                    fire('error', {type: 'parse', error: 'Unexpected message format'})
                                }
                                else
                                    return person;
                            },
                            save: function(callback) {
                                var url = this.root;
                                Y.io(url, {
                                    method: 'POST',
                                    data: Y.JSON.stringify({"person": this.toJSON()}),
                                    context: this,
                                    headers: {
                                        'Content-Type': 'application/json; charset=utf-8',
                                        'Accept': 'application/json',
                                    },
                                    on: {
                                        success: function(tid, xhr) {
                                            // doesn't want to load the +id attr
                                            var person = this.parse(xhr.responseText);
                                            this.set('id', person.id);
                                            this.fire("change");
                                            if (callback)
                                                callback();
                                        },
                                        failure: function(tid, xhr) {
                                            this.fire("error", {type: 'save', error: 'Cannot save the person', data: xhr});
                                            if (callback)
                                            {
                                                var m = (xhr.responseText) ? xhr.responseText : xhr.statusText;
                                                callback(m);
                                            }
                                        }
                                    }
                                });
                            },
                            ATTR: {
                                id: {},
                                name: {value: null},
                                address: {value: null},
                            }
                        });

                        Y.PersonList = Y.Base.create('persons', Y.ModelList, [Y.ModelSync.REST], {
//                     By convention `Y.User`'s `root` will be used for `Y.Users` as well.
                            root: '/services/blog/person',
                            url: '/services/blog/person',
                            model: Y.Person,
                            parse: function(response) {
                                var obj = Y.JSON.parse(response);
                                if (!obj.person)
                                {
                                    this.fire('error', {type: 'parse', error: 'Unexpected message format'})
                                    return;
                                }
                                return obj.person;
                            }
                        });




                        
}, '0.0.1', {requires: ['model','model-list','model-sync-rest','io','json-parse']});


