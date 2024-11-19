<template>
  <div class="relative w-full sm:w-auto">
    <select
      :value="modelValue?.code"
      @change="handleChange"
      :disabled="disabled"
      class="w-full sm:w-auto appearance-none bg-white border border-gray-300 rounded-md text-black pl-3 pr-8 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-orange-500 focus:border-orange-500 cursor-pointer"
    >
      <option value="">{{ placeholder }}</option>
      <option 
        v-for="option in options" 
        :key="option[valueKey]" 
        :value="option[valueKey]"
      >
        {{ option[labelKey] }}
      </option>
    </select>
    <div class="pointer-events-none absolute inset-y-0 right-0 flex items-center px-2 text-gray-400">
      <svg class="h-4 w-4" fill="currentColor" viewBox="0 0 20 20">
        <path fill-rule="evenodd" d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z" clip-rule="evenodd" />
      </svg>
    </div>
  </div>
</template>

<script setup>
const props = defineProps({
  modelValue: {
    type: [Object, String],
    default: ''
  },
  options: {
    type: Array,
    required: true
  },
  placeholder: {
    type: String,
    required: true
  },
  disabled: {
    type: Boolean,
    default: false
  },
  valueKey: {
    type: String,
    default: 'code'
  },
  labelKey: {
    type: String,
    default: 'name'
  }
});

const emit = defineEmits(['update:modelValue']);

const handleChange = (event) => {
  const selectedValue = event.target.value;
  if (!selectedValue) {
    emit('update:modelValue', '');
    return;
  }

  const selectedOption = props.options.find(
    option => option[props.valueKey].toString() === selectedValue
  );

  if (selectedOption) {
    emit('update:modelValue', {
      code: selectedOption[props.valueKey],
      name: selectedOption[props.labelKey]
    });
  }
};
</script>