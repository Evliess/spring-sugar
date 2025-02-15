module.exports = {
    // LaTex公式、yuml解析服务架设参见 https://github.com/sbfkcel/markdown-server

    // 数学公式解析API
    latex:{
        api:'http://towxml.vvadd.com/?tex'
    },

    // yuml图解析APPI
    yuml:{
        api:'http://towxml.vvadd.com/?yuml'
    },

    // markdown解析配置，保留需要的选项即可
    markdown:[
        'sub',                      // 下标支持
        'sup',                      // 上标支持
        'ins',                      // 文本删除线支持
        'mark',                     // 文本高亮支持
        'emoji',                    // emoji表情支持
        'todo'                      // todo支持
    ],

    // 代码高亮配置，保留需要的选项即可（尽量越少越好，不要随意调整顺序。部分高亮有顺序依赖）
    highlight:[
       
 
        
        // 'csharp',
        // 'http',
        // 'swift',
        // 'yaml',
        // 'markdown',
        // 'powershell',
        // 'ruby',
        // 'makefile',
        // 'lua',
        // 'stylus',
        // 'basic',
        // '1c',
        // 'abnf',
        // 'accesslog',
        // 'actionscript',
        // 'ada',
        // 'angelscript',
        // 'apache',
        // 'applescript',
        // 'arcade',
        // 'cpp',
        // 'arduino',
        // 'armasm',
        // 'asciidoc',
        // 'aspectj',
        // 'autohotkey',
        // 'autoit',
        // 'avrasm',
        // 'awk',
        // 'axapta',
        // 'bnf',
        // 'brainfuck',
        // 'cal',
        // 'capnproto',
        // 'ceylon',
        // 'clean',
        // 'clojure-repl',
        // 'clojure',
        // 'cmake',
        // 'coffeescript',
        // 'coq',
        // 'cos',
        // 'crmsh',
        // 'crystal',
        // 'csp',
        // 'd',
        // 'delphi',
        // 'diff',
        // 'django',
        // 'dns',
        // 'dockerfile',
        // 'dos',
        // 'dsconfig',
        // 'dts',
        // 'dust',
        // 'ebnf',
        // 'elixir',
        // 'elm',
        // 'erb',
        // 'erlang-repl',
        // 'erlang',
        // 'excel',
        // 'fix',
        // 'flix',
        // 'fortran',
        // 'fsharp',
        // 'gams',
        // 'gauss',
        // 'gcode',
        // 'gherkin',
        // 'glsl',
        // 'gml',
        // 'golo',
        // 'gradle',
        // 'groovy',
        // 'haml',
        // 'handlebars',
        // 'haskell',
        // 'haxe',
        // 'hsp',
        // 'hy',
        // 'inform7',
        // 'ini',
        // 'irpf90',
        // 'isbl',
        // 'jboss-cli',
        // 'julia-repl',
        // 'julia',
        // 'kotlin',
        // 'lasso',
        // 'latex',
        // 'ldif',
        // 'leaf',
        // 'lisp',
        // 'livecodeserver',
        // 'livescript',
        // 'llvm',
        // 'lsl',
        // 'mathematica',
        // 'matlab',
        // 'maxima',
        // 'mel',
        // 'mercury',
        // 'mipsasm',
        // 'mizar',
        // 'mojolicious',
        // 'monkey',
        // 'moonscript',
        // 'n1ql',
        // 'nim',
        // 'nix',
        // 'nsis',
        // 'objectivec',
        // 'ocaml',
        // 'openscad',
        // 'oxygene',
        // 'parser3',
        // 'perl',
        // 'pf',
        // 'pgsql',
        // 'php-template',
        // 'plaintext',
        // 'pony',
        // 'processing',
        // 'profile',
        // 'prolog',
        // 'properties',
        // 'protobuf',
        // 'puppet',
        // 'purebasic',
        // 'q',
        // 'qml',
        // 'r',
        // 'reasonml',
        // 'rib',
        // 'roboconf',
        // 'routeros',
        // 'rsl',
        // 'ruleslanguage',
        // 'rust',
        // 'sas',
        // 'scala',
        // 'scheme',
        // 'scilab',
        // 'smali',
        // 'smalltalk',
        // 'sml',
        // 'sqf',
        // 'sql',
        // 'stan',
        // 'stata',
        // 'step21',
        // 'subunit',
        // 'taggerscript',
        // 'tap',
        // 'tcl',
        // 'thrift',
        // 'tp',
        // 'twig',
        // 'vala',
        // 'vbnet',
        // 'vbscript-html',
        // 'vbscript',
        // 'verilog',
        // 'vhdl',
        // 'vim',
        // 'x86asm',
        // 'xl',
        // 'xquery',
        // 'zephir'
    ],

    // wxml原生标签，该系列标签将不会被转换
    wxml:[
        'view',
        'video',
        'text',
        'image',
        'navigator',
        'swiper',
        'swiper-item',
        'block',
        'form',
        'input',
        'textarea',
        'button',
        'checkbox-group',
        'checkbox',
        'radio-group',
        'radio',
        'rich-text',

        // 可以解析的标签（html或markdown中会很少使用）
        // 'canvas',
        // 'map',
        // 'slider',
        // 'scroll-view',
        // 'movable-area',
        // 'movable-view',
        // 'progress',
        // 'label',
        // 'switch',
        // 'picker',
        // 'picker-view',
        // 'switch',
        // 'contact-button'
    ],

    // 自定义组件
    components:[
        'latex',                    // 数学公式支持
        'table',                    // 表格支持
        'todogroup',                // todo支持
        'yuml',                     // yuml图表支持
        'img'                       // 图片解析组件
    ],

    // 保留原本的元素属性（建议不要变动）
    attrs:[
        'class',
        'data',
        'id',
        'style'
    ],

    // 事件绑定方式（catch或bind），catch 会阻止事件向上冒泡。更多请参考：https://developers.weixin.qq.com/miniprogram/dev/framework/view/wxml/event.html
    bindType:'catch',

    // 需要激活的事件
    events:[
        // 'touchstart',
        // 'touchmove',
        // 'touchcancel',
        // 'touchend',
        'tap',                      // 用于元素的点击事件
        'change',                   // 用于todoList的change事件
    ],

    // 图片倍数
    dpr:1,

    // 代码块显示行号
    showLineNumber:true
}