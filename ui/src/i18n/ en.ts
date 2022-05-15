// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

const entityFields = {
    id: 'Identifier',
    name: 'Name',
    description: 'Description',
    properties: 'Properties',
    statistics: 'Statistics',
    info: 'Information',
};

const entityHelpText = {
    name: 'A name referring to the instance',
    description: 'A brief description of the instance',
};

const en = {
    resources: {
        providers: {
            name: 'Provider |||| Providers',
            fields: {
                ...entityFields,
                datacenters: 'Owned Datacenters',
            },
            helpText: entityHelpText,
        },
        datacenters: {
            name: 'Datacenter |||| Datacenters',
            fields: {
                ...entityFields,
                hosts: 'Included Hosts',
            },
            helpText: entityHelpText,
        },
        hosts: {
            name: 'Host |||| Hosts',
            fields: entityFields,
            helpText: entityHelpText,
        },
        brokers: {
            name: 'Broker |||| Brokers',
            fields: {
                ...entityFields,
                vms: 'Owned Virtual Machines',
                cloudlets: 'Cloudlets (Workload)',
            },
            helpText: entityHelpText,
        },
        vms: {
            name: 'Virtual Machine |||| Virtual Machines',
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
        scenarios: {
            name: 'Scenario |||| Scenarios',
            fields: {
                entityFields,
                runs: 'Number of Iterations',
                entities: {
                    id: 'Instance',
                    amount: 'Amount',
                },
                provider: {
                    id: 'Service Provider',
                },
                brokers: 'Brokers (Customers)',
            },
            helpText: {
                name: 'A name referring to the scenario',
                description: 'A brief description of the scenario',
                runs: 'The number of times the scenario will run before submitting the results',
                entities: {
                    id: 'The name of the instance to be used',
                    amount: 'The amount of the instance to be added',
                },
            },
        },
        runs: {
            name: 'Experiment Run |||| Experiment Runs',
            fields: entityFields,
            helpText: entityHelpText,
        },
    },
    menu: {
        entities: 'Entities',
        experiments: 'Experiments',
    },
    label: {
        entity: {
            form: {
                basic: 'Basic Configuration',
                advanced: 'Advanced Configuration',
                hosts: 'Hosts Assignment',
            },
            button: {
                add_datacenter: 'Add new Datacenter',
                add_vm: 'Add new Virtual Machine',
                add_cloudlet: 'Add new Cloudlet',
                add_provider: 'Add new Provider',
                assign: 'Assign',
            },
        },
        provider: {
            tabs: {
                general: 'Configuration',
                datacenters: 'Datacenters Assignment',
            },
            button: {
                create: 'Continue',
                edit: 'Edit Provider',
                delete: 'Delete Provider',
            },
        },
        broker: {
            tabs: {
                general: 'Configuration',
                vms: 'Virtual Machines',
                cloudlets: 'Workload',
            },
            button: {
                create: 'Continue',
                edit: 'Edit Broker',
                delete: 'Delete Broker',
            },
        },
        scenario: {
            tabs: {
                providers: 'Provider Configuration',
            },
            action: {
                run: 'Run This Scenario',
            },
        },
        run: {
            create: {
                title: ': Run',
                save: 'Run',
            },
        },
    },
    action: {
        run: 'Run',
        back: 'back',
        new: 'Add New',
        info: 'Show More Info',
        continue: 'Continue',
    },
};

export default en;
