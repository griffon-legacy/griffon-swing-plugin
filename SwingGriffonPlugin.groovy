/*
 * Copyright 2008-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the 'License');
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an 'AS IS' BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * @author Andres Almiray
 */
class SwingGriffonPlugin {
    String version = '0.9.6'
    String griffonVersion = '0.9.6 > *'
    Map dependsOn = [:]
    List pluginIncludes = []
    String license = 'Apache Software License 2.0'
    List toolkits = ['swing']
    List platforms = []
    String documentation = ''
    String source = 'https://github.com/griffon/griffon-swing-plugin'

    List authors = [
        [
            name: 'Andres Almiray',
            email: 'aalmiray@yahoo.com'
        ]
    ]
    String title = 'Enables Swing support'
    String description = '''
Enables the usage of Swing based components in Views.

Usage
----
This plugin enables the usage of the following nodes inside a View.

### Swing Nodes

All nodes from [SwingBuilder][1]. All nodes follow a naming convention that makes it easy to
determine the type of object the create. Take for example *JButton*. Remove the first character
from the name (J) then uncapitalize the next one. You end up with *button*. That's the name
of the node you must use if what you want is to create an instance of a *JButton*. Apply the
same rule for every Swing class that begins with a capital J. For all other classes you only
need to uncapitalize the first character, for example *GridLayout* becomes *gridLayout*.

### Groovy Nodes

The following nodes are provided by [SwingBuilder][1] too, however they have no direct relationship
with a particualr Swing/AWT class.

#### widget
TBD

#### container
TBD

#### bean
TBD

#### noparent
TBD

### Griffon Nodes

#### application
TBD

#### root
TBD

Configuration
-------------
There's no special configuration for this plugin.

[1]: http://groovy.codehaus.org/Swing+Builder
'''
}
