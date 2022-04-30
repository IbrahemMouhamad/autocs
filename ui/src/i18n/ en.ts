// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

const entityFields = {
    id: 'Identifier',
    name: 'Name',
    description: 'Description',
    details: 'Details',
};

const entityHelpText = {
    name: 'A name referring to the instance',
    description: 'A brief description of the instance',
};

const en = {
    resources: {
        datacenters: {
            name: 'Datacenter |||| Datacenters',
            fields: entityFields,
            helpText: entityHelpText,
        },
        hosts: {
            name: 'Host |||| Hosts',
            fields: entityFields,
            helpText: entityHelpText,
        },
        brokers: {
            name: 'Broker |||| Brokers',
            fields: entityFields,
            helpText: entityHelpText,
        },
        vms: {
            name: 'VM |||| VMs',
            fields: entityFields,
            helpText: entityHelpText,
        },
        cloudlets: {
            name: 'Cloudlet |||| Cloudlets',
            fields: entityFields,
            helpText: entityHelpText,
        },
        configurations: {
            name: 'Configuration |||| Configuration',
            fields: {
                id: 'Identifier',
                name: 'Entity Name',
                description: 'Description',
                details: 'Details',
            },
        },
        experiments: {
            name: 'Experiment |||| Experiments',
            fields: {
                id: 'Identifier',
                name: 'Name',
                description: 'Description',
                runs: 'Number of Iterations',
                entities: {
                    id: 'Instance',
                    amount: 'Amount',
                },
            },
            helpText: {
                name: 'A name referring to the experiment',
                description: 'A brief description of the experiment',
                runs: 'The number of times the experiment will run before submitting the results',
                entities: {
                    id: 'The name of the instance to be used in the experiment',
                    amount: 'The amount of the instance to be added in the experiment',
                },
            },
        },
    },
    menu: {
        entities: 'Entities',
    },
    label: {
        entity: {
            form: {
                basic: 'Basic Configuration',
                advanced: 'Advanced Configuration',
            },
        },
        experiment: {
            form: {
                basic: 'Basic Configuration',
                datacenters: 'Datacenters Configuration',
                brokers: 'Brokers Configuration',
            },
        },
    },
    action: {
        run: 'Run',
    },
};

export default en;
