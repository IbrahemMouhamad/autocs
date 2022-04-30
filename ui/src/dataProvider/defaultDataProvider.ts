// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import { stringify } from 'query-string';
import {
    PaginationPayload,
    SortPayload,
    FilterPayload,
    DataProvider,
    fetchUtils,
} from 'react-admin';

export const baseUrl = (path: string): string => {
    if (path === undefined) return '';
    const parts = [process.env.REACT_APP_BACKEND_BASE_URL];
    parts.push(path.replace(/^\//, ''));
    return parts.join('/');
};

const getPaginationQuery = (pagination: PaginationPayload): {} => ({
    page: pagination.page,
    page_size: pagination.perPage,
});

const getFilterQuery = (filter: FilterPayload): {} => {
    const { q: search, ...otherSearchParams } = filter;
    return {
        ...otherSearchParams,
        search,
    };
};

export const getOrderingQuery = (sort: SortPayload): {} => {
    const { field, order } = sort;
    return {
        ordering: `${order === 'ASC' ? '' : '-'}${field}`,
    };
};

export default <DataProvider>{
    getList: async (resource, params) => {
        const query = {
            ...getFilterQuery(params.filter),
            ...getPaginationQuery(params.pagination),
            ...getOrderingQuery(params.sort),
        };

        const url = baseUrl(`/${resource}?${stringify(query)}`);
        const { json } = await fetchUtils.fetchJson(url);
        return {
            data: json,
            total: json.length,
        };
    },

    getOne: async (resource, params) => {
        const url = baseUrl(`/${resource}/${params.id}`);
        const { json } = await fetchUtils.fetchJson(url);
        return {
            data: json,
        };
    },

    getMany: (resource, params) => (
        Promise.all(
            params.ids.map(async (id) => {
                const url = baseUrl(`/${resource}/${id}`);
                const { json } = await fetchUtils.fetchJson(url);
                return json;
            }),
        ).then((data) => (
            {
                data,
            }
        ))
    ),

    getManyReference: async (resource, params) => {
        const query = {
            ...getFilterQuery(params.filter),
            ...getPaginationQuery(params.pagination),
            ...getOrderingQuery(params.sort),
            [params.target]: params.id,
        };
        const url = baseUrl(`/${resource}?${stringify(query)}`);
        const { json } = await fetchUtils.fetchJson(url);
        return {
            data: json,
            total: json.length,
        };
    },

    update: async (resource, params) => {
        const { json } = await fetchUtils.fetchJson(baseUrl(`/${resource}/${params.id}`), {
            method: 'PUT',
            body: JSON.stringify(params.data),
        });
        return { data: json };
    },

    updateMany: (resource, params) => Promise.all(
        params.ids.map((id) => fetchUtils.fetchJson(baseUrl(`/${resource}/${id}`), {
            method: 'PUT',
            body: JSON.stringify(params.data),
        })),
    ).then((responses) => ({ data: responses.map(({ json }) => json.id) })),

    create: async (resource, params) => {
        const { json } = await fetchUtils.fetchJson(baseUrl(`/${resource}`), {
            method: 'POST',
            body: JSON.stringify(params.data),
        });
        return {
            data: { ...json },
        };
    },

    delete: (resource, params) => fetchUtils.fetchJson(baseUrl(`/${resource}/${params.id}`), {
        method: 'DELETE',
    }).then((json) => ({ data: json })),

    deleteMany: (resource, params) => Promise.all(
        params.ids.map((id) => (
            fetchUtils.fetchJson(baseUrl(`/${resource}/${id}`), {
                method: 'DELETE',
            }))),
    ).then(() => ({ data: [] })),
};
