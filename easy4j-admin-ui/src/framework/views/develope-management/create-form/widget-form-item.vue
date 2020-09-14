<template>
  <el-form-item
    v-if="Object.keys(itemData).length"
    class="selected-item"
    :class="{ 'selected-item-active': itemData.key === selectWidget.key }"
    :label="itemData.name"
  >
    <template v-if="itemData.type == 'input'">
      <el-input
        v-model="itemData.options.defaultValue"
        :style="{width: itemData.options.width}"
        :placeholder="itemData.options.placeholder"
        :disabled="itemData.options.disabled"
      ></el-input>
    </template>

    <template v-if="itemData.type == 'textarea'">
      <el-input type="textarea" :rows="5"
        v-model="itemData.options.defaultValue"
        :style="{width: itemData.options.width}"
        :disabled="itemData.options.disabled"
        :placeholder="itemData.options.placeholder"
      ></el-input>
    </template>

    <template v-if="itemData.type == 'number'">
      <el-input-number
        v-model="itemData.options.defaultValue"
        :disabled="itemData.options.disabled"
        :controls-position="itemData.options.controlsPosition"
        :style="{width: itemData.options.width}"
      ></el-input-number>
    </template>

    <template v-if="itemData.type == 'radio'">
      <el-radio-group v-model="itemData.options.defaultValue"
        :style="{width: itemData.options.width}"
        :disabled="itemData.options.disabled"
      >
        <el-radio
          :style="{display: itemData.options.inline ? 'inline-block' : 'block'}"
          :label="item.value" v-for="(item, index) in itemData.options.options" :key="item.value + index"

        >
          {{itemData.options.showLabel ? item.label : item.value}}
        </el-radio>
      </el-radio-group>
    </template>

    <template v-if="itemData.type == 'checkbox'">
      <el-checkbox-group v-model="itemData.options.defaultValue"
        :style="{width: itemData.options.width}"
        :disabled="itemData.options.disabled"
      >
        <el-checkbox
          :style="{display: itemData.options.inline ? 'inline-block' : 'block'}"
          :label="item.value" v-for="(item, index) in itemData.options.options" :key="item.value + index"
        >
          {{itemData.options.showLabel ? item.label : item.value}}
        </el-checkbox>
      </el-checkbox-group>
    </template>

    <template v-if="itemData.type == 'time'">
      <el-time-picker
        v-model="itemData.options.defaultValue"
        :is-range="itemData.options.isRange"
        :placeholder="itemData.options.placeholder"
        :start-placeholder="itemData.options.startPlaceholder"
        :end-placeholder="itemData.options.endPlaceholder"
        :readonly="itemData.options.readonly"
        :disabled="itemData.options.disabled"
        :editable="itemData.options.editable"
        :clearable="itemData.options.clearable"
        :arrowControl="itemData.options.arrowControl"
        :style="{width: itemData.options.width}"
      >
      </el-time-picker>
    </template>

    <template v-if="itemData.type == 'date'">
      <el-date-picker
        v-model="itemData.options.defaultValue"
        :type="itemData.options.type"
        :is-range="itemData.options.isRange"
        :placeholder="itemData.options.placeholder"
        :start-placeholder="itemData.options.startPlaceholder"
        :end-placeholder="itemData.options.endPlaceholder"
        :readonly="itemData.options.readonly"
        :disabled="itemData.options.disabled"
        :editable="itemData.options.editable"
        :clearable="itemData.options.clearable"
        :style="{width: itemData.options.width}"
      >
      </el-date-picker>
    </template>

    <template v-if="itemData.type == 'rate'">
      <el-rate v-model="itemData.options.defaultValue"
        :max="itemData.options.max"
        :disabled="itemData.options.disabled"
        :allow-half="itemData.options.allowHalf"
      ></el-rate>
    </template>

    <template v-if="itemData.type == 'color'">
      <el-color-picker
        v-model="itemData.options.defaultValue"
        :disabled="itemData.options.disabled"
        :show-alpha="itemData.options.showAlpha"
      ></el-color-picker>
    </template>

    <template v-if="itemData.type == 'select'">
      <el-select
        v-model="itemData.options.defaultValue"
        :disabled="itemData.options.disabled"
        :multiple="itemData.options.multiple"
        :clearable="itemData.options.clearable"
        :placeholder="itemData.options.placeholder"
        :style="{width: itemData.options.width}"
      >
        <el-option v-for="item in itemData.options.options" :key="item.value" :value="item.value" :label="itemData.options.showLabel?item.label:item.value"></el-option>
      </el-select>
    </template>

    <template v-if="itemData.type=='switch'">
      <el-switch
        v-model="itemData.options.defaultValue"
        :disabled="itemData.options.disabled"
      >
      </el-switch>
    </template>

    <template v-if="itemData.type=='slider'">
      <el-slider
        v-model="itemData.options.defaultValue"
        :min="itemData.options.min"
        :max="itemData.options.max"
        :disabled="itemData.options.disabled"
        :step="itemData.options.step"
        :show-input="itemData.options.showInput"
        :range="itemData.options.range"
        :style="{width: itemData.options.width}"
      ></el-slider>
    </template>

    <template v-if="itemData.type=='imgupload'">
      <fm-upload
        v-model="itemData.options.defaultValue"
        :disabled="itemData.options.disabled"
        :style="{'width': itemData.options.width}"
        :width="itemData.options.size.width"
        :height="itemData.options.size.height"
        token="xxx"
        domain="xxx"
      >

      </fm-upload>
    </template>

    <template v-if="itemData.type == 'cascader'">
      <el-cascader
        v-model="itemData.options.defaultValue"
        :disabled="itemData.options.disabled"
        :clearable="itemData.options.clearable"
        :placeholder="itemData.options.placeholder"
        :style="{width: itemData.options.width}"
        :options="itemData.options.remoteOptions"
      >

      </el-cascader>
    </template>

    <template v-if="itemData.type == 'editor'">
      <vue-editor
        v-model="itemData.options.defaultValue"
        :style="{width: itemData.options.width}"
      >
      </vue-editor>
    </template>

    <template v-if="itemData.type=='blank'">
      <div style="height: 50px;color: #999;background: #eee;line-height:50px;text-align:center;">{{$t('fm.components.fields.blank')}}</div>
    </template>

    <template v-if="itemData.type == 'text'">
      <span>{{itemData.options.defaultValue}}</span>
    </template>

    <!-- <div class="widget-view-action" v-if="selectWidget.key == itemData.key">
      <i class="iconfont icon-icon_clone" @click.stop="handleWidgetClone(index)"></i>
      <i class="iconfont icon-trash" @click.stop="handleWidgetDelete(index)"></i>
    </div>

    <div class="widget-view-drag" v-if="selectWidget.key == itemData.key">
      <i class="iconfont icon-drag drag-widget"></i>
    </div> -->

  </el-form-item>
</template>

<script>
export default {
  components: {

  },
  props: {
    itemData: {
      type: Object,
      default: () => {},
    },
    select: {
      type: Object,
      default: () => {},
    },
  },
  data () {
    return {
      selectWidget: this.select
    }
  },
  watch: {
    select (val) {
      this.selectWidget = val

      console.log(this.itemData, 'itemData')
      console.log(this.selectWidget, 'selectWidget')
    },

    selectWidget: {
      handler (val) {
        this.$emit('update:select', val)
      },
      deep: true
    },
  },
  computed: {

  },
  mounted () {

  },
  methods: {

  }
}
</script>

<style scoped>

</style>
