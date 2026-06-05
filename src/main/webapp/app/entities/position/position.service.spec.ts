import { beforeEach, describe, expect, it, vi } from 'vitest';

import axios from 'axios';

import { Position } from '@/shared/model/position.model';

import PositionService from './position.service';

const error = {
  response: {
    status: null,
    data: {
      type: null,
    },
  },
};

const axiosStub = {
  get: vi.spyOn(axios, 'get'),
  post: vi.spyOn(axios, 'post'),
  put: vi.spyOn(axios, 'put'),
  patch: vi.spyOn(axios, 'patch'),
  delete: vi.spyOn(axios, 'delete'),
};

describe('Service Tests', () => {
  describe('Position Service', () => {
    let service: PositionService;
    let elemDefault;

    beforeEach(() => {
      service = new PositionService();
      elemDefault = new Position(123, 'AAAAAAA', 'AAAAAAA', 'TECHNICAL', 'HIGH', false, 'AAAAAAA', 0, 0, 'MONTHLY', false);
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = { ...elemDefault };
        axiosStub.get.mockResolvedValue({ data: returnedFromService });

        return service.find(123).then(res => {
          expect(res).toMatchObject(elemDefault);
        });
      });

      it('should not find an element', async () => {
        axiosStub.get.mockRejectedValue(error);
        return service
          .find(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should create a Position', async () => {
        const returnedFromService = { id: 123, ...elemDefault };
        const expected = { ...returnedFromService };

        axiosStub.post.mockResolvedValue({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a Position', async () => {
        axiosStub.post.mockRejectedValue(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a Position', async () => {
        const returnedFromService = {
          positionCode: 'BBBBBB',
          positionName: 'BBBBBB',
          positionType: 'BBBBBB',
          businessImportance: 'BBBBBB',
          keyPosition: true,
          description: 'BBBBBB',
          plannedHeadcount: 1,
          minimumOwnerCount: 1,
          reviewCycle: 'BBBBBB',
          active: true,
          ...elemDefault,
        };

        const expected = { ...returnedFromService };
        axiosStub.put.mockResolvedValue({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a Position', async () => {
        axiosStub.put.mockRejectedValue(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a Position', async () => {
        const patchObject = { keyPosition: true, description: 'BBBBBB', minimumOwnerCount: 1, active: true, ...new Position() };
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = { ...returnedFromService };
        axiosStub.patch.mockResolvedValue({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a Position', async () => {
        axiosStub.patch.mockRejectedValue(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of Position', async () => {
        const returnedFromService = {
          positionCode: 'BBBBBB',
          positionName: 'BBBBBB',
          positionType: 'BBBBBB',
          businessImportance: 'BBBBBB',
          keyPosition: true,
          description: 'BBBBBB',
          plannedHeadcount: 1,
          minimumOwnerCount: 1,
          reviewCycle: 'BBBBBB',
          active: true,
          ...elemDefault,
        };
        const expected = { ...returnedFromService };
        axiosStub.get.mockResolvedValue([returnedFromService]);
        return service.retrieve({ sort: {}, page: 0, size: 10 }).then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of Position', async () => {
        axiosStub.get.mockRejectedValue(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a Position', async () => {
        axiosStub.delete.mockResolvedValue({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a Position', async () => {
        axiosStub.delete.mockRejectedValue(error);

        return service
          .delete(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });
    });
  });
});
