const path = require('path')

function resolve(dir) {
  return path.join(__dirname, dir)
}

module.exports = {
  lintOnSave: false,
  css: {
    sourceMap: false,
    loaderOptions: {}
  },
  productionSourceMap: false,
  chainWebpack: config => {
    if (process.env.NODE_ENV === 'production') {
      config.optimization.minimizer.map((arg) => {
        const option = arg.options.terserOptions.compress
        option.drop_console = true
        return arg
      })
    }
    config.plugins.delete('prefetch')
  },
  configureWebpack: config => {
    config.resolve.extensions = [...config.resolve.extensions, '.js', '.vue', '.json']
    config.resolve.alias = {
      ...config.resolve.alias,
      'vue$': 'vue/dist/vue.esm.js',
      '@': resolve('src'),
      '@framework': resolve('src/framework'),
      '@biz': resolve('src/biz'),
      'components': resolve('src/framework/components'),
      'framework/views': resolve('src/framework/views'),
      '@mixins': resolve('src/framework/mixins')
    }
  },
  devServer: {
    disableHostCheck: true,
    proxy: {
      '/api': {
        target: 'http://127.0.0.1:6789',
        changeOrigin: true,
        ws: true,
        pathRewrite: {
          '^/api': ''
        }
      }
    }
  }
}
